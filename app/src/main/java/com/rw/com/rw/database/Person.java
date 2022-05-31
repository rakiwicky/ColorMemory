package com.rw.com.rw.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rakhita on 2/18/2018.
 */

@DatabaseTable(tableName = "Person")
public class Person {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int mId;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;

    @DatabaseField(columnName = "score")
    private Integer mScore;

    public Person() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }
}
