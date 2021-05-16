package ru.sapteh.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.dao.DAO;
import ru.sapteh.models.Client;
import ru.sapteh.service.ClientImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class MainWindowController {
    private SessionFactory factory;
    private DAO<Client, Integer> dao;
    private ObservableList<Client> observableList;
    Client client = new Client();

    @FXML
    private Button buttonCreate;

    @FXML
    private Button createEdit;

    @FXML
    private TableView<Client> tableView;

    @FXML
    private TableColumn<Client, Integer> idColumn;

    @FXML
    private TableColumn<Client, String> firstnameColumn;

    @FXML
    private TableColumn<Client, String> lastnameColumn;

    @FXML
    private TableColumn<Client, String> patronymicColumn;

    @FXML
    private TableColumn<Client, LocalDate> birthdayColumn;

    @FXML
    private TableColumn<Client, LocalDate> registrationColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> phoneColumn;

    @FXML
    private TableColumn<Client, Character> genderColumn;

    @FXML
    private TableColumn<Client, String> imageColumn;

    @FXML
    void initialize(){
        initData();
        tableView.setItems(observableList);
        idColumn.setCellValueFactory(clientIntegerCellDataFeatures -> new SimpleObjectProperty<>(clientIntegerCellDataFeatures.getValue().getId()));
        firstnameColumn.setCellValueFactory(clientStringCellDataFeatures -> new SimpleObjectProperty<>(clientStringCellDataFeatures.getValue().getFirstName()));
        lastnameColumn.setCellValueFactory(clientStringCellDataFeatures -> new SimpleObjectProperty<>(clientStringCellDataFeatures.getValue().getLastName()));
        patronymicColumn.setCellValueFactory(clientStringCellDataFeatures -> new SimpleObjectProperty<>(clientStringCellDataFeatures.getValue().getPatronymic()));
        birthdayColumn.setCellValueFactory(clientDateCellDataFeatures -> new SimpleObjectProperty<>(clientDateCellDataFeatures.getValue().getBirthday()));
        registrationColumn.setCellValueFactory(clientDateCellDataFeatures -> new SimpleObjectProperty<>(clientDateCellDataFeatures.getValue().getRegistrationDate()));
        emailColumn.setCellValueFactory(clientStringCellDataFeatures -> new SimpleObjectProperty<>(clientStringCellDataFeatures.getValue().getEmail()));
        phoneColumn.setCellValueFactory(clientStringCellDataFeatures -> new SimpleObjectProperty<>(clientStringCellDataFeatures.getValue().getPhone()));
        genderColumn.setCellValueFactory(clientCharacterCellDataFeatures -> new SimpleObjectProperty<>(clientCharacterCellDataFeatures.getValue().getGender().getCode()));
        imageColumn.setCellValueFactory(clientStringCellDataFeatures -> new SimpleObjectProperty<>(clientStringCellDataFeatures.getValue().getPhotoPath()));
        tableView.getSelectionModel().selectedItemProperty().addListener((obj, oldValue, newValue) ->{
            client = newValue;
        });
    }

    @FXML
    void actionCreate(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/create.fxml"));
        AnchorPane pane = loader.load();
        Stage stage = new Stage();
        stage.setTitle("CREATE");
        stage.setScene(new Scene(pane));
        CreateController createController = loader.getController();
        createController.setData(observableList);
        stage.show();
    }

    @FXML
    void actionDelete(ActionEvent event) {
        client = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(client);
        dao.delete(client);
    }

    @FXML
    void actionEdit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit.fxml"));
        AnchorPane pane = loader.load();
        Stage stage = new Stage();
        stage.setTitle("EDIT");
        stage.setScene(new Scene(pane));
        EditController editController = loader.getController();
        editController.setData(client, observableList);
        stage.show();
    }

    public void initData(){
        factory = new Configuration().configure().buildSessionFactory();
        dao = new ClientImpl(factory);
        observableList = FXCollections.observableArrayList();
        observableList.addAll(dao.findByAll());
    }

}
