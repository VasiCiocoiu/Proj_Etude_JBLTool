package fr.iutfbleau.jbltool.utils;

import java.util.function.Consumer;
import java.util.function.Function;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.collection.ListModification;
import javafx.application.Platform;

// https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsDemo.java
public class VisibleParagraphStyler <PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
    private final GenericStyledArea<PS, SEG, S> area;
    private final Function<String,StyleSpans<S>> computeStyles;

    public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String,StyleSpans<S>> computeStyles) {
        this.computeStyles = computeStyles;
        this.area = area;
    }

    @Override
    public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm) {
        if (lm.getAddedSize() > 0) {
            String text = area.getText();
            Highlighter h = new Highlighter(text);
            Platform.runLater(h);
        }
    }

    // this class is written here as it is used for only one thing in this class and it would be less readable
    // to write in its own file instead of here
    private class Highlighter implements Runnable {
        String text;

        public Highlighter(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            area.setStyleSpans(0, computeStyles.apply(text));
        }
    }
}