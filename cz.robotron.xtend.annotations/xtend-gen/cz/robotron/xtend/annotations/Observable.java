package cz.robotron.xtend.annotations;

import cz.robotron.xtend.annotations.ObservableCompilationParticipant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

/**
 * Adds a getter and setter method for all fields
 * Adds PropertyChangeSupport to this class and informs all listeners on change.
 */
@Target(ElementType.TYPE)
@Active(ObservableCompilationParticipant.class)
public @interface Observable {
}
