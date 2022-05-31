package com.rw.com.rw.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by Rakhita on 2/18/2018.
 */

public class PersonDaoProvider {

    private ConnectionSource mConnectionSource;

    public PersonDaoProvider(DatabaseHelper databaseHelper) {
        mConnectionSource = databaseHelper.getConnectionSource();
    }

    public Dao<Person, Integer> get() {
        try {
            return DaoManager.createDao(mConnectionSource, Person.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
