package todolist.youtube.com.codetutor.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import todolist.youtube.com.codetutor.MainScreenContract;
import todolist.youtube.com.codetutor.MyApplication;
import todolist.youtube.com.codetutor.model.Model;
import todolist.youtube.com.codetutor.model.bean.ToDo;

public class MainScreenPresenterImp implements MainScreenContract.Presenter {

    private static final String TAG = "MainScreenPresenterImp";

    MainScreenContract.View view;
    Model model;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Scheduler backgroundThread;

    public MainScreenPresenterImp(MainScreenContract.View view) {
        this.view = view;
        this.model = MyApplication.getModel();
        this.view.setPresenter(this);
    }

    public MainScreenPresenterImp(MainScreenContract.View view, Model model) {
        this.view = view;
        this.model = model;
        this.view.setPresenter(this);
    }

    // One way of testing threading
    public MainScreenPresenterImp(MainScreenContract.View view, Model model, Scheduler backgroundThread) {
        this.view = view;
        this.model = model;
        this.backgroundThread = backgroundThread;
    }

    @Override
    public void start() {
        try {
//            this.view.showAllToDos(model.getAllToDos());
            updateListUI();

        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onAddButtonClicked(String toDoItem, String place) {
        try {

            Log.d(TAG, "onAddButtonClicked: Process " + Thread.currentThread().getId());
            DisposableSingleObserver<Boolean> disposableSingleObserver = new DisposableSingleObserver<Boolean>() {
                @Override
                public void onSuccess(Boolean val) {
                    if (val) {
                        updateListUI();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    view.showError(e.getMessage());
                }
            };

            compositeDisposable.add(disposableSingleObserver);
            model.addToDoItemReactivly(toDoItem, place)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(disposableSingleObserver);

        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    private void updateListUI() {
        DisposableSingleObserver<List<ToDo>> disposableSingleObserver = new DisposableSingleObserver<List<ToDo>>() {
            @Override
            public void onSuccess(List<ToDo> toDos) {
                view.showAllToDos(toDos);
            }

            @Override
            public void onError(Throwable e) {

            }
        };

        compositeDisposable.add(disposableSingleObserver);
        model.getAllToDosReactivlly()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableSingleObserver);
    }

    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void onToDoItemSelected(long toDoId) {
        view.navigateToDataManipulationActivity(toDoId);
    }
}
