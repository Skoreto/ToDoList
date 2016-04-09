package cz.uhk.fim.skoreto.todolist.model;

/**
 * Created by Tomas.
 */
public class TaskList {

    private int id;

    private String name;

    public TaskList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
