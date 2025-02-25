package org.example._4equals10;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.text.DecimalFormat;
import java.util.*;

public class LevelController {

    // FXML UI elements
    @FXML
    private TabPane tabPane;
    @FXML
    private Button startGameButton;
    @FXML
    private Tab level1Tab;
    @FXML
    private Tab level2Tab;
    @FXML
    private Tab level3Tab;
    @FXML
    private Tab extraLevelTab;

    // Level 1 UI elements
    @FXML
    private Label number1, operator1, number2, operator2, number3, operator3, number4;
    @FXML
    private Button plusButton, minusButton, multiplyButton, divideButton;
    @FXML
    private Label resultLabel;

    // Level 2 UI elements
    @FXML
    private Label number11, operator11, number21, operator21, number31, operator31, number41;
    @FXML
    private Button plusButton1, minusButton1, multiplyButton1, divideButton1;
    @FXML
    private Label resultLabel1;

    // Level 3 UI elements
    @FXML
    private Label number12, operator12, number22, operator22, number32, operator32, number42;
    @FXML
    private Button plusButton2, minusButton2, multiplyButton2, divideButton2;
    @FXML
    private Label resultLabel2;
    @FXML
    private WebView solver;

    // EXTRA Level UI elements
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Label number13, number23, number33, number43;
    @FXML
    private Label operator13, operator23, operator33;
    @FXML
    private Button plusButton3, minusButton3, multiplyButton3, divideButton3;
    @FXML
    private Label resultLabel3;

    // Media for background music and sound effects
    private MediaPlayer backgroundMusic;
    private AudioClip effectSound;
    private AudioClip solveSound;
    private AudioClip incorrectSound;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    public void initialize() {
        // Load sound effects
        effectSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/org/example/_4equals10/EffectSound.mp3")).toExternalForm());
        solveSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/org/example/_4equals10/nice.mp3")).toExternalForm());
        incorrectSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/org/example/_4equals10/nope.mp3")).toExternalForm());

        // Load and play background music
        try {
            Media media = new Media(Objects.requireNonNull(getClass().getResource("/org/example/_4equals10/BackgroundMusic.mp3")).toExternalForm());
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop the background music
            backgroundMusic.setVolume(0.1);
            backgroundMusic.play();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Background Music failed to load!");
        }

        // Setup initial configurations for the game
        setupWebView();
        setupChoiceBox();

        // Setup drag-and-drop functionality for Level 1 operators
        setupDragAndDrop(plusButton);
        setupDragAndDrop(minusButton);
        setupDragAndDrop(multiplyButton);
        setupDragAndDrop(divideButton);
        setupDropTarget(operator1);
        setupDropTarget(operator2);
        setupDropTarget(operator3);

        // Setup drag-and-drop functionality for Level 2 operators
        setupDragAndDrop(plusButton1);
        setupDragAndDrop(minusButton1);
        setupDragAndDrop(multiplyButton1);
        setupDragAndDrop(divideButton1);
        setupDropTarget(operator11);
        setupDropTarget(operator21);
        setupDropTarget(operator31);

        // Setup drag-and-drop functionality for Level 3 operators
        setupDragAndDrop(plusButton2);
        setupDragAndDrop(minusButton2);
        setupDragAndDrop(multiplyButton2);
        setupDragAndDrop(divideButton2);
        setupDropTarget(operator12);
        setupDropTarget(operator22);
        setupDropTarget(operator32);

        // Setup drag-and-drop functionality for EXTRA level operators
        setupDragAndDrop(plusButton3);
        setupDragAndDrop(minusButton3);
        setupDragAndDrop(multiplyButton3);
        setupDragAndDrop(divideButton3);
        setupDropTarget(operator13);
        setupDropTarget(operator23);
        setupDropTarget(operator33);

        // Allow numbers to be dragged and swapped in Level 2 and Level 3
        setupNumberDragAndDrop(number11);
        setupNumberDragAndDrop(number21);
        setupNumberDragAndDrop(number31);
        setupNumberDragAndDrop(number41);

        setupNumberDragAndDrop(number12);
        setupNumberDragAndDrop(number22);
        setupNumberDragAndDrop(number32);
        setupNumberDragAndDrop(number42);

        setupNumberDragAndDrop(number13);
        setupNumberDragAndDrop(number23);
        setupNumberDragAndDrop(number33);
        setupNumberDragAndDrop(number43);

        // Disable all level tabs initially
        level1Tab.setDisable(true);
        level2Tab.setDisable(true);
        level3Tab.setDisable(true);
        extraLevelTab.setDisable(true);

        // Set action for start game button to unlock Level 1
        startGameButton.setOnAction(event -> unlockLevel1());
    }

    // Setup choice box for EXTRA level
    private void setupChoiceBox(){
        choiceBox.getItems().addAll("Extra 1", "Extra 2", "Extra 3"
        );
        // Update numbers based on the selected extra challenge
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "Extra 1":
                        updateExtraNumbers(7, 4, 0, 7);
                        break;
                    case "Extra 2":
                        updateExtraNumbers(6, 4, 7, 2);
                        break;
                    case "Extra 3":
                        updateExtraNumbers(8, 5, 9, 6);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // Unlock Level 1 and switch to it
    private void unlockLevel1() {
        level1Tab.setDisable(false);
        tabPane.getSelectionModel().select(level1Tab);
    }

    // Handle drag detected event for operators
    @FXML
    private void handleDragDetected(javafx.scene.input.MouseEvent event) {
        Button sourceButton = (Button) event.getSource();
        Dragboard db = sourceButton.startDragAndDrop(TransferMode.COPY);
        ClipboardContent content = new ClipboardContent();
        content.putString(sourceButton.getText());
        db.setContent(content);
        event.consume();
    }

    // Setup drag-and-drop for buttons (operators)
    private void setupDragAndDrop(Button button) {
        button.setOnDragDetected(this::handleDragDetected);
    }

    // Setup drop target for labels (operators)
    private void setupDropTarget(Label label) {
        label.setOnDragOver(event -> {
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        label.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                label.setText(db.getString());
                success = true;
                evaluateExpression();
                effectSound.play();
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Setup drag-and-drop for number labels (swapping)
    private void setupNumberDragAndDrop(Label label) {
        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            db.setContent(content);
            event.consume();
        });

        label.setOnDragOver(event -> {
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        label.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String temp = label.getText();
                label.setText(db.getString());
                Label sourceLabel = (Label) event.getGestureSource();
                sourceLabel.setText(temp);
                success = true;
                evaluateExpression();
                effectSound.play();
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Handle level completion and unlock next level
    private void handleLevelCompletion(){
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.getText().equals("Level 1")) {
            tabPane.getTabs().get(2).setDisable(false); // Enable Level 2 tab
        } else if (selectedTab.getText().equals("Level 2")) {
            tabPane.getTabs().get(3).setDisable(false); // Enable Level 3 tab
        } else {
            tabPane.getTabs().get(4).setDisable(false); // Enable Level EXTRA tab
        }
    }

    // Setup web view for the online solver tab
    private void setupWebView() {
        WebEngine engine = solver.getEngine();
        engine.load("https://eigilnikolajsen.dk/4is10-solver/");
        solver.setZoom(0.75);
    }

    // Convert infix expression to postfix
    private String infixToPostfix(String expression) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operators = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch)) {
                output.append(ch);
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                output.append(' '); // Add space to separate numbers and operators
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    output.append(operators.pop()).append(' ');
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            output.append(' ').append(operators.pop());
        }

        return output.toString();
    }

    // Check the precedence of operators
    private int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> -1;
        };
    }

    // Evaluate postfix expression
    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix.split("\\s+")) {
            if (token.matches("\\d+")) {
                stack.push(Double.valueOf(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> stack.push(a / b);
                }
            }
        }
        return stack.pop();
    }

    // Method to evaluate the Math Expression
    private double evaluateMathExpression(String expression) {
        // Convert infix expression to postfix using Shunting Yard algorithm
        String postfix = infixToPostfix(expression);

        // Evaluate postfix expression
        return evaluatePostfix(postfix);
    }

    // Evaluate Logic (Main)
    private void evaluateExpressionForLevel(Label num1, Label op1, Label num2, Label op2, Label num3, Label op3, Label num4, Label resultLabel) {
        String[] operators = {op1.getText(), op2.getText(), op3.getText()};
        if (isExpressionComplete(operators)) {
            try {
                String expression = num1.getText() + op1.getText() + num2.getText() + op2.getText() + num3.getText() + op3.getText() + num4.getText();
                double result = evaluateMathExpression(expression);
                resultLabel.setText("Result: " + df.format(result));
                if (Objects.equals(resultLabel.getText(), "Result: 10.00")){ // If the result equals 10, play a sound and enable next level
                    solveSound.play();
                    handleLevelCompletion();
                } else {
                    incorrectSound.play();
                }
            } catch (Exception e) {
                resultLabel.setText("Error");
            }
        }
    }

    private boolean isExpressionComplete(String[] operators) {
        for (String operator : operators) {
            if (operator.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Apply the evaluation logic to each level
    private void evaluateExpression() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.getText().equals("Level 1")) {
            evaluateExpressionForLevel(number1, operator1, number2, operator2, number3, operator3, number4, resultLabel);
        } else if (selectedTab.getText().equals("Level 2")) {
            evaluateExpressionForLevel(number11, operator11, number21, operator21, number31, operator31, number41, resultLabel1);
        } else if (selectedTab.getText().equals("Level 3")) {
            evaluateExpressionForLevel(number12, operator12, number22, operator22, number32, operator32, number42, resultLabel2);
        } else if (selectedTab.getText().equals("EXTRA")) {
            evaluateExpressionForLevel(number13, operator13, number23, operator23, number33, operator33, number43, resultLabel3);
        }
    }

    // Update number labels for the EXTRA level
    private void updateExtraNumbers(int num1, int num2, int num3, int num4) {
        number13.setText(String.valueOf(num1));
        number23.setText(String.valueOf(num2));
        number33.setText(String.valueOf(num3));
        number43.setText(String.valueOf(num4));

        // Clear Operator Content
        operator13.setText(" ");
        operator23.setText(" ");
        operator33.setText(" ");

        // Reset Result Label Content
        resultLabel3.setText("Result: ");
    }
}
