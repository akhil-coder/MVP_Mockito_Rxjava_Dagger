package todolist.youtube.com.codetutor.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;
import todolist.youtube.com.codetutor.DataManipulationScreenContract;
import todolist.youtube.com.codetutor.R;
import todolist.youtube.com.codetutor.model.bean.ToDo;
import todolist.youtube.com.codetutor.presenter.DataManipulationScreenPresenterImp;

public class DataManipulationActivity extends AppCompatActivity implements DataManipulationScreenContract.View {

    TextView textViewToBeModifiedToDoId, textViewToBeModifiedToDo, textViewToBeModifiedToDoPlace;
    Button buttonRemoveToDo, buttonModifyToDo;
    EditText editTextNewToDo;

    DataManipulationScreenContract.Presenter presenter;

    long toDoId;
    ToDo toDo;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_data_manipulate);

        toDoId = getIntent().getLongExtra("todoId",1);
        presenter = new DataManipulationScreenPresenterImp(this, toDoId);

        textViewToBeModifiedToDoId  = (TextView)findViewById(R.id.textViewToBeModifiedToDoId);
        textViewToBeModifiedToDo = (TextView)findViewById(R.id.textViewToBeModifiedToDo);
        textViewToBeModifiedToDoPlace = (TextView)findViewById(R.id.textViewToBeModifiedToDoPlace);

        buttonRemoveToDo = (Button)findViewById(R.id.buttonRemoveToDo);
        buttonModifyToDo = (Button)findViewById(R.id.buttonModifyToDo);

        editTextNewToDo = (EditText)findViewById(R.id.editTextNewToDo);

//        buttonRemoveToDo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.onRemoveButtonClicked(toDoId);
//            }
//        });

        buttonModifyToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onModifyButtonClicked(toDoId,editTextNewToDo.getText().toString());
            }
        });


        RxView.clicks(buttonRemoveToDo)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        presenter.onRemoveButtonClicked(toDoId);
                        updateViewOnRemove();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this,errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSelectedToDo(ToDo toDo) {
        try{
            textViewToBeModifiedToDoId.setText("Id: "+ toDo.getId());
            textViewToBeModifiedToDo.setText("ToDo: "+toDo.getToDo());
            textViewToBeModifiedToDoPlace.setText("Place: "+toDo.getPlace());
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateViewOnRemove() {
        buttonRemoveToDo.setEnabled(false);
        buttonModifyToDo.setEnabled(false);
        textViewToBeModifiedToDoId.setText("");
        textViewToBeModifiedToDo.setText("");
        textViewToBeModifiedToDoPlace.setText("");
        Toast.makeText(this,"Successfully removed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnModify(ToDo toDo) {
        this.toDo = toDo;
        textViewToBeModifiedToDo.setText(this.toDo.getToDo());
        Toast.makeText(this,"Successfully updated", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(DataManipulationScreenContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
