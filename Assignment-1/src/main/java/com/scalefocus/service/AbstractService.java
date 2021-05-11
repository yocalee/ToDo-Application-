package com.scalefocus.service;

import com.scalefocus.model.BaseEntity;
import com.scalefocus.repository.DataRepository;

import java.util.List;

public abstract class AbstractService<E extends BaseEntity> {

    private final DataRepository<E> dataRepository;

    protected AbstractService(DataRepository<E> dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<E> findAll() {
        return dataRepository.findAll();
    }

    public E findById(Integer id) {
        return dataRepository.findById(id).isPresent() ? dataRepository.findById(id).get() : null;
    }
    public void save(E entity){
        if (entity == null){
            throw new IllegalArgumentException("Entity must not be null.");
        }
        dataRepository.save(entity);
    }

    public E update(E entity){
        if (entity == null ){
            throw new IllegalArgumentException("Entity must not be null.");
        }
        return dataRepository.update(entity.getId(), entity);
    }

    public void delete(E entity){
        if (entity == null){
            throw new IllegalArgumentException("Entity must not be null.");
        }
        dataRepository.delete(entity.getId());
    }

}
