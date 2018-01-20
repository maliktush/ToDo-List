package com.tusharmalik.todowithdb.Models;

/**
 * Created by tushm on 08-01-2018.
 */

public class Todo {
    String task;
    boolean done;
    int id;

    public Todo(int id,String string, boolean b) {
        id=id;
        task=string;
        done=b;
    }

    public Todo() {

    }

    public boolean isChecked() {
        return done;
    }
    public String getData() {
        return task;
    }
    public void setChecked(boolean done) {
        this.done = done;
    }
    public void setData(String task) {
        this.task = task;
    }
}
