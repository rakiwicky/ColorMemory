package com.rw.com.rw.database;

import com.j256.ormlite.dao.Dao;

import java.util.List;

/**
 * Created by Rakhita on 2/18/2018.
 */

public class PersonRepo implements IPersonRepo {

    private Dao<Person, Integer> mPersonDao;

    public PersonRepo(Dao<Person, Integer> personDao) {
        this.mPersonDao = personDao;
    }

    @Override
    public List GetPersons() throws java.sql.SQLException {
        return mPersonDao.query(mPersonDao.queryBuilder().orderBy("Score", false).prepare());
    }

    @Override
    public void SavePerson(Person person) throws java.sql.SQLException {
        mPersonDao.create(person);
    }
}