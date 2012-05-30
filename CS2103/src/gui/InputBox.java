package gui;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.text.*;


public class InputBox {
    JComboBox comboBox;
    ComboBoxModel model;
    JTextComponent editor;
    
    KeyListener editorKeyListener;
    FocusListener editorFocusListener;

}
