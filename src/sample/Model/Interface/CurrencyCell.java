package sample.Model.Interface;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.UnaryOperator;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class CurrencyCell<T> extends TableCell<T, Float> {

    private final TextField textField ;

    private final NumberFormat format = DecimalFormat.getNumberInstance();
    private final DecimalFormat textFieldFormat = new DecimalFormat("0.00");

    public CurrencyCell() {
        this.textField = new TextField();
        StringConverter<Float> converter = new StringConverter<Float>() {

            @Override
            public String toString(Float object) {
                return object == null ? "" : textFieldFormat.format(object) ;
            }

            @Override
            public Float fromString(String string) {
                try {
                    return string.isEmpty() ? 0 : textFieldFormat.parse(string).floatValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return Float.valueOf(0);
                }
            }

        };
        UnaryOperator<TextFormatter.Change> filter = (TextFormatter.Change change) -> {
            String newText = change.getControlNewText() ;
            if (newText.isEmpty()) {
                return change ;
            }
            try {
                textFieldFormat.parse(newText);
                return change ;
            } catch (ParseException exc) {
                return null ;
            }
        };
        TextFormatter<Float> textFormatter = new TextFormatter<Float>(converter, (float) 0, filter);
        textField.setTextFormatter(textFormatter);

        textField.setOnAction(e -> commitEdit(converter.fromString(textField.getText())));
        textField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        setGraphic(textField);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

    }

    @Override
    protected void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else if (isEditing()) {
            textField.setText(item.toString());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            setText(format.format(item));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(textFieldFormat.format(getItem()));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(format.format(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(Float newValue) {
        super.commitEdit(newValue);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}