package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ReturnOfThis")
public class Notification /*extends Notifications*/ {

    @NotNull
    private final Notifications notifImpl;

    public Notification() {
        notifImpl = Notifications.create();
    }

    @NotNull
    public Notification text(String text) {
        notifImpl.text(text);
        return this;
    }

    @NotNull
    public Notification title(String title) {
        notifImpl.title(title);
        return this;
    }

    @NotNull
    public Notification graphic(Node graphic) {
        notifImpl.graphic(graphic);
        return this;
    }

    @NotNull
    public Notification position(Pos position) {
        notifImpl.position(position);
        return this;
    }

    @NotNull
    public Notification hideAfter(@NotNull Duration duration) {
        notifImpl.hideAfter(duration);
        return this;
    }

    @NotNull
    public Notification onAction(EventHandler<ActionEvent> onAction) {
        notifImpl.onAction(onAction);
        return this;
    }

    @NotNull
    public Notification darkStyle() {
        notifImpl.darkStyle();
        return this;
    }

    @NotNull
    public Notification hideCloseButton() {
        notifImpl.hideCloseButton();
        return this;
    }

    @NotNull
    public Notification action(Action... actions) {
        notifImpl.action(actions);
        return this;
    }

    public void showWarning() {
        notifImpl.showWarning();
    }

    public void showInformation() {
        notifImpl.showInformation();
    }

    public void showError() {
        notifImpl.showError();
    }

    public void showConfirm() {
        notifImpl.showConfirm();
    }

    public void show() {
        notifImpl.show();
    }

    public void hide() {
        notifImpl.hideAfter(Duration.seconds(1)); // FIXME FDA 2017/08 Ne fonctionne pas, sans doute parce que la nouvelle Duration est ignorée, vu le code.
    }
}
