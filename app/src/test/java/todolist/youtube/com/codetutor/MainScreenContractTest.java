package todolist.youtube.com.codetutor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import todolist.youtube.com.codetutor.model.Model;
import todolist.youtube.com.codetutor.model.bean.ToDo;
import todolist.youtube.com.codetutor.presenter.MainScreenPresenterImp;

public class MainScreenContractTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    MainScreenContract.View view;

    @Mock
    Model model;
    private MainScreenPresenterImp mainScreenPresenterImp;
    private List<ToDo> toDoList;


    @Before
    public void setUp() throws Exception {
        mainScreenPresenterImp = new MainScreenPresenterImp(view, model, Schedulers.trampoline());
        toDoList = new ArrayList<>();
        //Set Ioscheduler to trampoline
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @After
    public void cleanUp() {
        RxJavaPlugins.reset();  // Clears any changes to shedulers
    }

    @Test
    public void shouldAddEntriesToViewOnAddClick() throws Exception {

        toDoList.add(new ToDo(1, "Drink", "Sun"));
        toDoList.add(new ToDo(2, "Drink", "Mars"));
        toDoList.add(new ToDo(3, "Drink", "Pluto"));

        Mockito.when(model.addToDoItem("Piss", "Moon")).thenReturn(true);
        Mockito.when(model.getAllToDosReactivlly()).thenReturn(Single.just(toDoList));

        //When
        mainScreenPresenterImp.onAddButtonClicked("Piss", "Moon");

        //Then
        Mockito.verify(view).updateViewOnAdd(toDoList);
    }

//    @Test
//    public void shouldHandleError(){
//
//        toDoList.add(new ToDo(1, "Drink", "Sun"));
//        toDoList.add(new ToDo(2, "Drink", "Mars"));
//        toDoList.add(new ToDo(3, "Drink", "Pluto"));
//
//        Mockito.when(model.addToDoItem("Piss", "Moon")).thenReturn(true);
//        Mockito.when(model.getAllToDos()).thenThrow(new RuntimeException("Boom"));
//
//        //When
//        mainScreenPresenterImp.onAddButtonClicked("Piss", "Moon");
//
//        //Then
//        Mockito.verify(view).showError("Boom");
//    }

    @Test
    public void shouldLaunchDataManipOnItemClick() {

        //When
        MainScreenPresenterImp mainScreenPresenterImp = new MainScreenPresenterImp(view, model);
        mainScreenPresenterImp.onToDoItemSelected(1);
        //Then
        Mockito.verify(view).navigateToDataManipulationActivity(1);
    }

}