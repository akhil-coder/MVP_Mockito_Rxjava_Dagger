package todolist.youtube.com.codetutor.model;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import todolist.youtube.com.codetutor.exception.ToDoNotFoundException;
import todolist.youtube.com.codetutor.model.bean.ToDo;
import todolist.youtube.com.codetutor.model.db.ToDoListDBAdapter;


/*      TODO
  Convert rest of db adapter operations into reactive */

public class ModelImplementor implements Model {

    private static final String TAG = "ModelImplementor";
    ToDoListDBAdapter toDoListDBAdapter;

    List<ToDo> toDoItems;

    public ModelImplementor(ToDoListDBAdapter toDoListDBAdapter){
        this.toDoListDBAdapter = toDoListDBAdapter;
        toDoItems = this.toDoListDBAdapter.getAllToDos();
    }

    @Override
    public Single<List<ToDo>> getAllToDosReactivlly() {

        return Single.fromCallable(new Callable<List<ToDo>>() {
            @Override
            public List<ToDo> call() throws Exception {
                Log.d(TAG, "call: Process id: " + Thread.currentThread().getId());
                return getAllToDos();
            }
        });
    }

    @Override
    public List<ToDo> getAllToDos() throws Exception{
        this.toDoItems = this.toDoListDBAdapter.getAllToDos();
        if(this.toDoItems!=null && this.toDoItems.size()>0){
            return this.toDoItems;
        } else {
          throw new Exception("Empty To Do List");
        }
    }

    @Override
    public Single<Boolean> addToDoItemReactivly(final String toDoItem, final String place) throws Exception {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean insertSuccess = toDoListDBAdapter.insert(toDoItem, place);
                if (insertSuccess){
                    refresh();
                }else{
                    throw new Exception("Some thing went wrong!!!");
                }
                return insertSuccess;
            }
        });
    }

    @Override
    public Single<Boolean> removeToDoItemReactivly(final long id) throws Exception {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean insertSuccess = toDoListDBAdapter.delete(id);
                if (insertSuccess){
                    refresh();
                }else{
                    throw new ToDoNotFoundException("Id is wrong");
                }
                return insertSuccess;
            }
        });    }

    @Override
    public boolean addToDoItem(String toDoItem, String place) throws Exception{
        boolean addSuccess = toDoListDBAdapter.insert(toDoItem, place);
        if (addSuccess){
            refresh();
        }else{
            throw new Exception("Some thing went wrong!!!");
        }

        return addSuccess;
    }

    @Override
    public boolean removeToDoItem(long id) throws Exception{

        boolean deleteSuccess = toDoListDBAdapter.delete(id);
        if(deleteSuccess){
            refresh();
        }else{
            throw new ToDoNotFoundException("Id is wrong");
        }
        return deleteSuccess;

    }

    @Override
    public boolean modifyToDoItem(long id, String newToDoValuel) throws Exception{
        boolean modifySuccess = toDoListDBAdapter.modify(id,newToDoValuel);
        if(modifySuccess){
            refresh();
        } else{
            throw new ToDoNotFoundException("Id is wrong");
        }
        return modifySuccess;
    }



    public ToDo getToDo(long id) throws Exception{
        ToDo toDo = null;
        for(ToDo toDo1: toDoItems){
            if(toDo1.getId()==id){
                toDo = toDo1;
                break;
            }
        }
        if(toDo==null){
            throw new ToDoNotFoundException("Id is wrong");
        }
        return toDo;
    }

    private void refresh(){
        toDoItems.clear();
        toDoItems = this.toDoListDBAdapter.getAllToDos();
    }
}
