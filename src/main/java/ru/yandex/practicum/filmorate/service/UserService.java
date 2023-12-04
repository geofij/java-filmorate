package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.feed.Event;
import ru.yandex.practicum.filmorate.model.feed.Operation;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;
    private final LikesStorage likesStorage;
    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage,
                       FriendsStorage friendsStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikesStorage likesStorage,
                       FeedStorage feedStorage) {
        this.userStorage = storage;
        this.friendsStorage = friendsStorage;
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
        this.feedStorage = feedStorage;
    }

    public void create(User user) {
        userStorage.create(user);
    }

    public User update(User user) {
        userStorage.getById(user.getId());
        return userStorage.update(user);
    }

    public boolean delete(long id) {
        return userStorage.delete(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllData();
    }

    public User get(long id) {
        return userStorage.getById(id);
    }

    public void addFriend(long idUser, long idFriend) {
        userStorage.getById(idUser);
        userStorage.getById(idFriend);

        friendsStorage.addFriend(idUser, idFriend);
        feedStorage.addFeed(idUser, Event.FRIEND, Operation.ADD, idFriend);
    }

    public boolean deleteFriend(long idUser, long idFriend) {
        userStorage.getById(idUser);
        userStorage.getById(idFriend);
        feedStorage.addFeed(idUser, Event.FRIEND, Operation.REMOVE, idFriend);

        return friendsStorage.deleteFriend(idUser, idFriend);
    }

    public List<User> getFriends(long id) {
        userStorage.getById(id);
        return friendsStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(long idFirstUser, long idSecondUser) {
        userStorage.getById(idFirstUser);
        userStorage.getById(idSecondUser);

        Set<User> friendsFirst = new HashSet<>(friendsStorage.getUserFriends(idFirstUser));
        Set<User> friendsSecond = new HashSet<>(friendsStorage.getUserFriends(idSecondUser));

        if (friendsFirst.isEmpty() || friendsSecond.isEmpty()) {
            return new ArrayList<>();
        }

        friendsFirst.retainAll(friendsSecond);

        return new ArrayList<>(friendsFirst);
    }

    public Set<Film> getRecommendations(long id) {
        Set<Film> recommendedFilms = new HashSet<>();
        List<SimpleEntry<Long, Long>> filmLikes = likesStorage.getAllData();
        Set<Long> likedFilmsByUser = findLikesByUser(id, filmLikes);
        //вспомогательня мапа, содержащая ранг совпадения предпочтений "нашего" пользователя с остальными пользователями
        Map<Long, Integer> freq = new HashMap<>();
        //вспомогательная мапа, содержащая набор id фильмов, которые полайкал "другой" пользователь, и не лайкнул "наш"
        Map<Long, Set<Long>> diff = new HashMap<>();
        rankUsersPreferences(id, filmLikes, likedFilmsByUser, freq, diff);
        populateFilmRecommendationsBasedOnRank(recommendedFilms, freq, diff);
        return recommendedFilms;
    }

    private Set<Long> findLikesByUser(long userId, List<SimpleEntry<Long, Long>> filmLikes) {
        //составляем набор фильмов, понравившихся "нашему" пользователю, для которого мы составляем рекомендации
        return filmLikes.stream()
                .filter(e -> e.getKey().equals(userId))
                .map(SimpleEntry::getValue)
                .collect(Collectors.toSet());
    }

    private void populateFilmRecommendationsBasedOnRank(Set<Film> recommendedFilms, Map<Long, Integer> freq,
                                                        Map<Long, Set<Long>> diff) {
        //сортируем мапу с рангами
        Map<Long, Integer> sortedFreq = freq.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        for (Map.Entry<Long, Integer> entry : sortedFreq.entrySet()) {
            if (diff.containsKey(entry.getKey()) && diff.get(entry.getKey()).size() > 0) {
                for (Long filmId : diff.get(entry.getKey())) {
                    recommendedFilms.add(filmStorage.getById(filmId));
                }
                break;
            }
        }
    }

    private void rankUsersPreferences(long userId, List<SimpleEntry<Long, Long>> filmLikes, Set<Long> likedFilmsByUser,
                                      Map<Long, Integer> ranks, Map<Long, Set<Long>> diff) {
        for (SimpleEntry<Long, Long> like : filmLikes) {
            //проходимся по лайкам фильмов, оставленным не "нашим" пользователем и заполняем мапу с рангами
            if (!like.getKey().equals(userId)) {
                if (likedFilmsByUser.contains(like.getValue())) {
                    if (!ranks.containsKey(userId)) {
                        ranks.put(userId, 1);
                    } else {
                        //увеличиваем ранг совпадения предпочтений "нашего" пользователя и "другого" пользователя
                        ranks.put(userId, ranks.get(userId) + 1);
                    }
                } else {
                    //сохраняем id фильмов, которые "наш" не лайкнул, а "другие" лайкнули во вспомогательную мапу
                    if (!diff.containsKey(userId)) {
                        diff.put(userId, new HashSet<>(Arrays.asList(like.getValue())));
                    } else {
                        diff.get(userId).add(like.getValue());
                    }
                }
            }
        }
    }
}
