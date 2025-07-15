package com.demo.librarymanagementsystemgui.controller;

import com.demo.librarymanagementsystemgui.entity.Book;
import com.demo.librarymanagementsystemgui.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class BookController {
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, Integer> colId;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colIsbn;
    @FXML
    private TableColumn<Book, Integer> colStock;
    @FXML
    private TableColumn<Book, String> colPublisher;
    @FXML
    private TableColumn<Book, Void> colAction;
    @FXML
    private TableColumn<Book, Boolean> colSelect;

    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfIsbn;
    @FXML
    private TextField tfStock;
    @FXML
    private TextField tfPublisher;
    @FXML
    private TextField tfSearch;
    @FXML
    private Label lblPageInfo;
    @FXML
    private Label lblTotalCount;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private ComboBox<Integer> cbPageSize;

    private final BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    // 用于记录每行的选中状态
    private final ObservableList<Book> selectedBooks = FXCollections.observableArrayList();

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private String currentKeyword = "";

    private void updatePageInfo(int total) {
        totalPage = (int) Math.ceil(total * 1.0 / pageSize);
        if (totalPage == 0) totalPage = 1;
        lblPageInfo.setText("第" + currentPage + "页/共" + totalPage + "页");
        lblTotalCount.setText("总记录：" + total);
    }

    private void loadBooksPaged() {
        int total = bookService.countBooks(currentKeyword);
        updatePageInfo(total);
        bookList.setAll(bookService.searchBooks(currentKeyword, currentPage, pageSize));
        tableView.setItems(bookList);
    }

    @FXML
    private void onSearch(ActionEvent event) {
        currentKeyword = tfSearch.getText().trim();
        currentPage = 1;
        loadBooksPaged();
    }

    @FXML
    private void onPrevPage(ActionEvent event) {
        if (currentPage > 1) {
            currentPage--;
            loadBooksPaged();
        }
    }

    @FXML
    private void onNextPage(ActionEvent event) {
        if (currentPage < totalPage) {
            currentPage++;
            loadBooksPaged();
        }
    }

    // 添加图书弹窗
    @FXML
    private void onAddBookDialog(ActionEvent event) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("添加图书");
        dialog.setHeaderText("请输入图书信息");
        ButtonType addButtonType = new ButtonType("添加", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("书名");
        TextField authorField = new TextField();
        authorField.setPromptText("作者");
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        TextField stockField = new TextField();
        stockField.setPromptText("库存");
        TextField publisherField = new TextField();
        publisherField.setPromptText("出版社");

        grid.add(new Label("书名："), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("作者："), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN："), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("库存："), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("出版社："), 0, 4);
        grid.add(publisherField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                String stockStr = stockField.getText();
                String publisher = publisherField.getText();
                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
                    showAlert("请填写完整信息");
                    return null;
                }
                int stock;
                try {
                    stock = Integer.parseInt(stockStr);
                } catch (NumberFormatException e) {
                    showAlert("库存必须为数字");
                    return null;
                }
                return new Book(0, title, author, isbn, stock, publisher);
            }
            return null;
        });
        dialog.showAndWait().ifPresent(book -> {
            if (bookService.addBook(book)) {
                showAlert("添加成功");
                loadBooksPaged();
            } else {
                showAlert("添加失败");
            }
        });
    }

    // 批量删除
    @FXML
    private void onBatchDelete(ActionEvent event) {
        ObservableList<Book> toDelete = FXCollections.observableArrayList();
        for (Book book : bookList) {
            if (book.isSelected()) {
                toDelete.add(book);
            }
        }
        if (toDelete.isEmpty()) {
            showAlert("请先勾选要删除的图书");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除选中的" + toDelete.size() + "本图书吗？", ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("批量删除确认");
        confirm.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                boolean allSuccess = true;
                for (Book book : toDelete) {
                    if (!bookService.deleteBook(book.getId())) {
                        allSuccess = false;
                    }
                }
                if (allSuccess) {
                    showAlert("批量删除成功");
                } else {
                    showAlert("部分删除失败");
                }
                loadBooksPaged();
            }
        });
    }

    @FXML
    public void initialize() {
        tableView.setEditable(true);
        colSelect.setEditable(true);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        addSelectColumn();
        addActionColumn();
        currentKeyword = "";
        // 设置分页下拉框默认值和监听
        cbPageSize.setValue(pageSize);
        cbPageSize.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal > 0) {
                pageSize = newVal;
                currentPage = 1;
                loadBooksPaged();
            }
        });
        loadBooksPaged();
    }

    private void addSelectColumn() {
        colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
    }

    private void addActionColumn() {
        colAction.setCellFactory(param -> new TableCell<Book, Void>() {
            private final Button btnEdit = new Button("编辑");
            private final Button btnDelete = new Button("删除");
            private final HBox pane = new HBox(10, btnEdit, btnDelete);

            {
                btnEdit.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showEditDialog(book);
                });
                btnDelete.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除该图书吗？", ButtonType.OK, ButtonType.CANCEL);
                    confirm.setTitle("删除确认");
                    confirm.showAndWait().ifPresent(type -> {
                        if (type == ButtonType.OK) {
                            if (bookService.deleteBook(book.getId())) {
                                showAlert("删除成功");
                                loadBooksPaged();
                            } else {
                                showAlert("删除失败");
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void showEditDialog(Book book) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("编辑图书");
        dialog.setHeaderText("修改图书信息");
        ButtonType updateButtonType = new ButtonType("保存", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField isbnField = new TextField(book.getIsbn());
        TextField stockField = new TextField(String.valueOf(book.getStock()));
        TextField publisherField = new TextField(book.getPublisher());

        grid.add(new Label("书名："), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("作者："), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN："), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("库存："), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("出版社："), 0, 4);
        grid.add(publisherField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                String stockStr = stockField.getText();
                String publisher = publisherField.getText();
                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
                    showAlert("请填写完整信息");
                    return null;
                }
                int stock;
                try {
                    stock = Integer.parseInt(stockStr);
                } catch (NumberFormatException e) {
                    showAlert("库存必须为数字");
                    return null;
                }
                return new Book(book.getId(), title, author, isbn, stock, publisher);
            }
            return null;
        });
        dialog.showAndWait().ifPresent(updatedBook -> {
            if (bookService.updateBook(updatedBook)) {
                showAlert("修改成功");
                loadBooksPaged();
            } else {
                showAlert("修改失败");
            }
        });
    }

    private void loadBooks() {
        bookList.setAll(bookService.getAllBooks());
        tableView.setItems(bookList);
    }

    @FXML
    private void onAdd(ActionEvent event) {
        String title = tfTitle.getText();
        String author = tfAuthor.getText();
        String isbn = tfIsbn.getText();
        String stockStr = tfStock.getText();
        String publisher = tfPublisher.getText();
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
            showAlert("请填写完整信息");
            return;
        }
        int stock;
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            showAlert("库存必须为数字");
            return;
        }
        Book book = new Book(0, title, author, isbn, stock, publisher);
        if (bookService.addBook(book)) {
            showAlert("添加成功");
            loadBooksPaged();
            clearInput();
        } else {
            showAlert("添加失败");
        }
    }

    @FXML
    private void onUpdate(ActionEvent event) {
        Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("请选择要修改的图书");
            return;
        }
        String title = tfTitle.getText();
        String author = tfAuthor.getText();
        String isbn = tfIsbn.getText();
        String stockStr = tfStock.getText();
        String publisher = tfPublisher.getText();
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || stockStr.isEmpty()) {
            showAlert("请填写完整信息");
            return;
        }
        int stock;
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            showAlert("库存必须为数字");
            return;
        }
        Book book = new Book(selected.getId(), title, author, isbn, stock, publisher);
        if (bookService.updateBook(book)) {
            showAlert("修改成功");
            loadBooksPaged();
        } else {
            showAlert("修改失败");
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("请选择要删除的图书");
            return;
        }
        if (bookService.deleteBook(selected.getId())) {
            showAlert("删除成功");
            loadBooksPaged();
            clearInput();
        } else {
            showAlert("删除失败");
        }
    }

    @FXML
    private void onReset(ActionEvent event) {
        tfSearch.clear();
        currentKeyword = "";
        currentPage = 1;
        pageSize = 10;
        cbPageSize.setValue(10);
        loadBooksPaged();
    }

    private void clearInput() {
        tfTitle.clear();
        tfAuthor.clear();
        tfIsbn.clear();
        tfStock.clear();
        tfPublisher.clear();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
} 