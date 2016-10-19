package com.semeniuc.dmitrii.clientmanager.repository;

import java.util.List;

interface Repository
{
    int create(Object item);

    int update(Object item);

    int delete(Object item);

    Object findById(int id);

    List<?> findAll();
}
