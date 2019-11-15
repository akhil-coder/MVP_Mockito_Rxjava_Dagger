package todolist.youtube.com.codetutor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import todolist.youtube.com.codetutor.model.Model;
import todolist.youtube.com.codetutor.model.bean.ToDo;
import todolist.youtube.com.codetutor.presenter.MainScreenPresenterImp;

public class MainScreenContractTest {

    @Test
    public void shouldAddEntriesToViewOnAddClick(){

        //Given
        MainScreenContract.View view = new MockView();
        MockModel mockModel = new MockModel();

        //When
        MainScreenPresenterImp mainScreenPresenterImp = new MainScreenPresenterImp(view, mockModel);
        mainScreenPresenterImp.onAddButtonClicked("Piss", "Moon" );

        //Then
        Assert.assertEquals(true, ((MockView) view).passed);
    }

    @Test
    public void shouldLaunchDataManipOnItemClick() {
        //Given
        MainScreenContract.View view = new MockView();
        MockModel mockModel = new MockModel();

        //When
        MainScreenPresenterImp mainScreenPresenterImp = new MainScreenPresenterImp(view, mockModel);
        mainScreenPresenterImp.onToDoItemSelected(1);
        //Then
        Assert.assertEquals(true, ((MockView) view).passed);
    }


    private class MockView implements MainScreenContract.View {

        boolean passed;

        @Override
        public void showAllToDos(List<ToDo> toDoList) {

        }

        @Override
        public void updateViewOnAdd(List<ToDo> toDoList) {
             passed = true;
        }

        @Override
        public void showError(String errorMessage) {

        }

        @Override
        public void navigateToDataManipulationActivity(long id) {
            if(id == 1)
            passed = true;
        }

        @Override
        public void setPresenter(MainScreenContract.Presenter presenter) {

        }
    }

    private class MockModel implements Model{

        @Override
        public List<ToDo> getAllToDos() throws Exception {
            return null;
        }

        @Override
        public ToDo getToDo(long id) throws Exception {
            return null;
        }

        @Override
        public boolean addToDoItem(String toDoItem, String place) throws Exception {
            if(!toDoItem.isEmpty() && !place.isEmpty())
            return true;
            return false;
        }

        @Override
        public boolean removeToDoItem(long id) throws Exception {
            return false;
        }

        @Override
        public boolean modifyToDoItem(long id, String newToDoValuel) throws Exception {
            return false;
        }
    }
}