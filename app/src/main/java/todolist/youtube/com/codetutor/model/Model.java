package todolist.youtube.com.codetutor.model;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import todolist.youtube.com.codetutor.model.bean.ToDo;

public interface Model {

    List<ToDo> getAllToDos() throws Exception;

    ToDo getToDo(long id) throws Exception;

    boolean addToDoItem(String toDoItem, String place) throws Exception;

    boolean removeToDoItem(long id) throws Exception;

    boolean modifyToDoItem(long id, String newToDoValuel) throws Exception;

    Single<List<ToDo>> getAllToDosReactivlly();

    Single<Boolean> addToDoItemReactivly(String toDoItem, String place) throws Exception;

    Single<Boolean> removeToDoItemReactivly(long id) throws Exception;

}
