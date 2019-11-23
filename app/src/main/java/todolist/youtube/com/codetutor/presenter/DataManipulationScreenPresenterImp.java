package todolist.youtube.com.codetutor.presenter;

import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import todolist.youtube.com.codetutor.DataManipulationScreenContract;
import todolist.youtube.com.codetutor.MyApplication;
import todolist.youtube.com.codetutor.model.Model;

public class DataManipulationScreenPresenterImp implements DataManipulationScreenContract.Presenter {


    DataManipulationScreenContract.View view;
    Model model;
    long toDoId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DataManipulationScreenPresenterImp(DataManipulationScreenContract.View view){
        this.view = view;
        this.model = MyApplication.getModel();
        this.view.setPresenter(this);
    }

    public DataManipulationScreenPresenterImp(DataManipulationScreenContract.View view, long toDoId){
        this.view = view;
        this.model = MyApplication.getModel();
        this.toDoId = toDoId;
    }

    @Override
    public void start() {
        try {
            view.showSelectedToDo(model.getToDo(toDoId));
        }catch (Exception e){
            view.showError(e.getMessage());
        }

    }

    @Override
    public void onModifyButtonClicked(long id, String newValue) {
        try{
            boolean success = model.modifyToDoItem(id,newValue);
            if(success){
                view.updateViewOnModify(model.getToDo(id));
            }
        }catch (Exception e){
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onRemoveButtonClicked(long id) {

            DisposableSingleObserver<Boolean> disposableSingleObserver = new DisposableSingleObserver<Boolean>() {
                @Override
                public void onSuccess(Boolean val) {
                    if (val) {
                        view.updateViewOnRemove();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    view.showError(e.getMessage());
                }
            };

        compositeDisposable.add(disposableSingleObserver);
        try {
            model.removeToDoItemReactivly(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(disposableSingleObserver);
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
