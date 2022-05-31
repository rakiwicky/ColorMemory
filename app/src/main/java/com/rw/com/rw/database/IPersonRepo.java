package com.rw.com.rw.database;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rakhita on 2/18/2018.
 */

public interface IPersonRepo {

    List GetPersons() throws SQLException;

    void SavePerson(Person savePerson) throws SQLException;
}
