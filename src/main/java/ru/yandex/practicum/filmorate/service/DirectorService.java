package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Director create(Director data) {
        return directorStorage.create(data);
    }

    public void update(Director data) {
        directorStorage.update(data);
    }

    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirector();
    }

    public Director get(long id) {
        return directorStorage.getById(id);
    }

    public boolean delete(long id) {
        return directorStorage.delete(id);
    }
}
