package todolist.youtube.com.codetutor.model.bean;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by anildeshpande on 4/3/17.
 */

public class ToDo {
    /**
     * Created by anildeshpande on 4/3/17.
     */

    private long id;
    private String toDo;
    private String place;

    public ToDo(){
        super();
    }

    public ToDo(long id, String toDo){
        this.id=id;
        this.toDo=toDo;
    }

    public ToDo(long id, String toDo, String place){
        this.id=id;
        this.toDo=toDo;
        this.place=place;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "("+ id+", "+toDo+", "+place+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ToDo){
            ToDo temp = (ToDo) obj;
            if(temp.id==this.id){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
