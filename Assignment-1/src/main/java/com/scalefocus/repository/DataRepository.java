package com.scalefocus.repository;


import com.scalefocus.exception.EntityFetchException;
import com.scalefocus.model.BaseEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public abstract class DataRepository<E extends BaseEntity> {
    private BufferedReader bufferedReader;
    private BufferedWriter bWriter;
    private File file;

    public DataRepository(String fileName) throws IOException {
        if (fileName == null || "".equals(fileName.trim())) {
            throw new IllegalArgumentException("Enter valid filename.");
        }
        file = new File(fileName);
        bWriter = new BufferedWriter(new FileWriter(file, true));
    }

    public List<E> findAll() {
        try {
            List<E> entityList = new LinkedList<>();
            List<String> lines = Files.readAllLines(Path.of(file.getPath()));
            for (String line : lines) {
                String[] data = line.split("\\*\\*\\*\\*");
                entityList.add(buildEntity(data));
            }
            return entityList;
        } catch (IOException e) {
            throw new EntityFetchException("An error occur while trying to fetch records.");
        }
    }

    public Optional<E> findById(int id) {
        try {
            E entity = null;
            List<String[]> lines = Files.readAllLines(Path.of(file.getPath()))
                    .stream().filter(l -> !l.isEmpty()).map(l -> l.split("\\*\\*\\*\\*")).collect(Collectors.toList());

            for (String[] line : lines) {
                if (Integer.parseInt(line[0]) == id) {
                    entity = buildEntity(line);
                    break;
                }
            }
            return Optional.of(entity);
        } catch (IOException e) {
            throw new EntityFetchException("An error occur while trying to fetch record.");
        }
    }

    public void save(E entity) {
        try {
            writeEntity(entity);
            bWriter.write("\n");
        } catch (IOException e) {
            throw new EntityFetchException("Exception occur while trying to save data.");
        }
    }

    public E update(int id, E entity) {
        delete(id);
        writeEntity(entity);
        return entity;
    }

    public void delete(int id) {
        Optional<E> en = findById(id);
        String toRemove = en.toString().trim().replace("Optional[", "").replace("]", "");
        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(Path.of(file.getPath())));
            List<String> out = new ArrayList<>();
            for (String line : lines) {
                if (!line.trim().equals(toRemove)) {
                    out.add(line);
                }
            }
            this.bWriter = Files.newBufferedWriter(file.toPath(), StandardOpenOption.TRUNCATE_EXISTING);
            out.stream().filter(l -> !l.isEmpty()).forEach(l -> {
                writeEntity(buildEntity(l.split("\\*\\*\\*\\*")));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeEntity(E entity) {
        try {
            bWriter.write(entity.toString());
            bWriter.write("\n");
            bWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract E buildEntity(String[] data);

    public int getNextId() {
        Random random = new Random();
        int id = random.nextInt(19) + random.nextInt(600);
        try {
            if (findById(id).isPresent()) {
                id = random.nextInt(19) + random.nextInt(600);
            }
            return id;
        }catch (NullPointerException e) {
            return id;
        }
    }
}
