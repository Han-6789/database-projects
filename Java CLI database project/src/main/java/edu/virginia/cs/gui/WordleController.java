//package edu.virginia.cs.gui;
//import edu.virginia.cs.wordle.Wordle;
//import edu.virginia.cs.wordle.WordleImplementation;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyEvent;
//import java.util.*;
//
//public class WordleController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
//
////    The GUI must have the following elements:
////    The game is started as soon as the application is opened. No "click here to start" or instructions screen
////    is needed, nor should one be present.
////    At least 6 places to enter text (such as TextFields or Groups of text fields) where players can enter answers.
////    However, once a player has entered a valid 5-letter word, that field should be "locked" (that is, the user can
////    no longer edit it to change their answer).
////    When writing a word, a user should be able to enter a word simply by typing 5 letters.
////    For example, if you use 5 separate TextField objects (one for each letter),
////    then you must handle moving the cursor to the next TextField *for the user*. For example, the user should be able to type:
////            "MATCH"
////            …and not have to do something like…
////            "M" (click on next textfield), "A" (click on next textfield), "T"...
//
////    You must have actual colors, Yellow, Gray, and Green to help with visual recognition.
////    The text of the letters should be white.
////    All previous user guesses and their color results from the current game must be visible on the screen at all times.
////    If the player guesses the correct word (hasWon() is true), then the game should end.
////    The user should not be able to fill in any more text fields.
////    If the player runs out of guesses (that is, guesses incorrectly 6 times - hasLost() is true)
////    then the game should tell the player they lost and also tell them the correct word.
////    In both cases, text should appear asking the user if they want to play again.
////    There should be two Buttons: Yes and No. If they hit No, then close the application.
////    If they hit Yes, then generate a new WordleGame and reset the GUI to the starting state.
////    If a user enters an invalid word, your UI should inform the user of the error and allow them to enter another word.
////    Your program should never crash, all exceptions should be handled with meaningful
////    feedback to the user to inform them what they did wrong.
//    private Wordle wordleGame;
//    private List<TextField> textFields;
//    TextField tf1,tf2,tf3,tf4,tf5;
//
//
//    public void initialize() {
//        wordleGame = new WordleImplementation();
//        textFields = new ArrayList<>();
//        tf1 = new TextField();
//        tf2 = new TextField();
//        tf3 = new TextField();
//        tf4 = new TextField();
//        tf5 = new TextField();
//        textFields.add(tf1);
//        textFields.add(tf2);
//        textFields.add(tf3);
//        textFields.add(tf4);
//        textFields.add(tf5);
//    }
//
//    //Define Textfield, handle the movement of the cursor. It goes to the next textfield
//    //when the user types in a letter.
//    @FXML
//    protected void onWordEnter(KeyEvent event) {
//        if(isLetter(event.getText())) {
//            //set each textfield
//            for(int i = 0; i < textFields.size(); i++) {
//                textFields.get(i).setText(event.getText());
//
//            }
//        }
//    }
//
//private boolean isLetter(String str) {
//        if(str == null || str.length() != 1) {
//            return false;
//        }
//        char c = str.charAt(0);
//        if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
//            return true;
//        }
//        return false;
//}
//
//
//
//
//}

package edu.virginia.cs.gui;

import edu.virginia.cs.wordle.IllegalWordException;
import edu.virginia.cs.wordle.LetterResult;
import edu.virginia.cs.wordle.Wordle;
import edu.virginia.cs.wordle.WordleImplementation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class WordleController {
    @FXML
    TextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9, tf10,
            tf11, tf12, tf13, tf14, tf15, tf16, tf17, tf18, tf19, tf20,
            tf21, tf22, tf23, tf24, tf25, tf26, tf27, tf28, tf29, tf30;

    private List<TextField> textFields = null;
    private int count = 0;
    private Wordle wordle = null;

    @FXML
    private void initialize() {
        textFields = new ArrayList<>();
        wordle = new WordleImplementation();
        textFields.add(tf1);
        textFields.add(tf2);
        textFields.add(tf3);
        textFields.add(tf4);
        textFields.add(tf5);
        textFields.add(tf6);
        textFields.add(tf7);
        textFields.add(tf8);
        textFields.add(tf9);
        textFields.add(tf10);
        textFields.add(tf11);
        textFields.add(tf12);
        textFields.add(tf13);
        textFields.add(tf14);
        textFields.add(tf15);
        textFields.add(tf16);
        textFields.add(tf17);
        textFields.add(tf18);
        textFields.add(tf19);
        textFields.add(tf20);
        textFields.add(tf21);
        textFields.add(tf22);
        textFields.add(tf23);
        textFields.add(tf24);
        textFields.add(tf25);
        textFields.add(tf26);
        textFields.add(tf27);
        textFields.add(tf28);
        textFields.add(tf29);
        textFields.add(tf30);


        System.out.println(wordle.getAnswer());

        textFields.forEach(e -> {
            e.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (isLetter(keyEvent.getText())) {
                        textFields.get(count).setText(keyEvent.getText().toUpperCase());

                        ++count;
                        if (count % 5 == 0) {
                            String guess = textFields.get(count - 5).getText() +
                                    textFields.get(count - 4).getText() +
                                    textFields.get(count - 3).getText() +
                                    textFields.get(count - 2).getText() +
                                    textFields.get(count - 1).getText();
                            try {
                                LetterResult[] result = wordle.submitGuess(guess);
                                for (int i = 0; i < result.length; i++) {
                                    LetterResult r = result[i];
                                    if (r.equals(LetterResult.GRAY)) {
                                        textFields.get(count - (5 - i)).setStyle("-fx-background-color:GRAY;fx-border-color:gray;-fx-text-fill:white");
                                        //textFields.get(count - (5 - i)).getText().toUpperCase();
                                    } else if (r.equals(LetterResult.YELLOW)) {
                                        textFields.get(count - (5 - i)).setStyle("-fx-background-color:#C3C326;fx-border-color:gray;-fx-text-fill:white");
                                       // textFields.get(count - (5 - i)).getText().toUpperCase();
                                    } else if (r.equals(LetterResult.GREEN)) {
                                        textFields.get(count -  (5 - i)).setStyle("-fx-background-color:GREEN;fx-border-color:gray;-fx-text-fill:white");
                                        //textFields.get(count - (5 - i)).getText().toUpperCase();
                                    }
                                }
                            } catch (IllegalWordException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, guess + " is not a valid word!Try again!");
                                alert.showAndWait();
                                textFields.get(count - 5).setText(null);
                                textFields.get(count - 4).setText(null);
                                textFields.get(count - 3).setText(null);
                                textFields.get(count - 2).setText(null);
                                textFields.get(count - 1).setText(null);
                                count -= 5;
                            }
                        }
                        //if (count == 30) count = 0;

                        if (wordle.isWin()) {


                            //System.out.println("Congratulations! You won in " + wordle.getGuessCount() + " guesses!");
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Congratulations! You won in "
                                    + wordle.getGuessCount() + " guesses!\nYou want to play again?",ButtonType.YES,
                                    ButtonType.NO);
                            //alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                            //set the button type in the alert to yes or no
                            alert.getDialogPane().lookupButton(ButtonType.YES).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    wordle = new WordleImplementation();
                                    System.out.println(wordle.getAnswer());
                                    count = 0;
                                    for (TextField t : textFields) {
                                        t.setText(null);
                                        t.setStyle("-fx-background-color:White;fx-border-color:gray");
                                    }
                                }
                            });
                            alert.getDialogPane().lookupButton(ButtonType.NO).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    System.exit(0);
                                }
                            });
                            alert.showAndWait();
                        } else if (wordle.isLoss()) {
                            System.out.println("Sorry, you are out of guesses... The word was " + wordle.getAnswer());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sorry, you are out of guesses... " +
                                    "The word was " + wordle.getAnswer() + " !\nYou want to play again?",ButtonType.NO,
                                    ButtonType.YES);
                            alert.getDialogPane().lookupButton(ButtonType.YES).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    wordle = new WordleImplementation();
                                    System.out.println(wordle.getAnswer());
                                    count = 0;
                                    for (TextField t : textFields) {
                                        t.setText(null);
                                        t.setStyle("-fx-background-color:White;fx-border-color:gray");
                                    }
                                }
                            });
                            alert.getDialogPane().lookupButton(ButtonType.NO).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    System.exit(0);
                                }
                            });
                            alert.showAndWait();
                        }
                    } else if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
                        if (count % 5 == 0) return;
                        --count;
                        textFields.get(count).setText(null);
                    }
                }
            });
        });

    }

    private boolean isLetter(String str) {
        if (str == null || str.length() != 1) return false;
        char c = str.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }
}
