package GUI.Components;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import GUI.GUI;
import GUI.Animations.ClickAnimation;
import GUI.Popups.PopupDisplay;
import Main.CurrencyCalculator;
import Utils.Utils;
import Utils.Data.Calculations;
import Utils.Data.Config.Settings.LastCalculation;
import lang.Language;

public class Miscellaneous {

    private static JButton saveBtn = new JButton();
    private static JButton loadBtn = new JButton();
    private static JLabel presetLbl = new JLabel();
    private static JLabel fadeLbl = new JLabel();
    private static JLabel clipboardLblBtn = new JLabel();
    private static JLabel swapLblBtn = new JLabel();
    private static JLabel authorLbl = new JLabel();

    public static void addAllComponents() {
        addSwapCurrenciesLbl();
        addCopyOutputLbl();
        addPresetLbl();
        addSaveCalculationButton();
        addLoadLastCalculationButton();
        addCustomFadeLbl();
        addFooter();

        setComponentBounds();
    }

    /*
     * Diese Methode setzt die Größen und Positionen der jeweiligen Komponenten
     */
    private static void setComponentBounds() {

        /*
         * TODO @Leon hab das ganze jetzt mittig zentriert. Es müsste eigentlich auf -40
         * sein, habe es jetzt aber auf -45 gemacht, weil es mir so vorkam, als
         * wäre es nicht zentriert -> Kannst den Kommentar danach löschen
         */
        swapLblBtn.setBounds((GUI.getWindowWidth() - 45) / 2, 200, 80, 20);

        clipboardLblBtn.setBounds(420, 405, 100, 30);

        loadBtn.setBounds(50, 480, 100, 25);
        authorLbl.setBounds(15, GUI.getWindowHeight() - 60, 200, 20);

        presetLbl.setBounds(50, 420, 100, 25);
        fadeLbl.setBounds(200, 450, 100, 25);
    }

    /*
     * TODO
     */
    private static void addSwapCurrenciesLbl() {
        swapLblBtn.setText("↔");
        swapLblBtn.setFont(swapLblBtn.getFont().deriveFont(36f));
        swapLblBtn.setComponentPopupMenu(null);

        swapLblBtn.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object selectedItem1 = InputOutput.getComboBoxes()[0].getSelectedItem();
                Object selectedItem2 = InputOutput.getComboBoxes()[1].getSelectedItem();

                // Set the selected items to the opposite combo boxes
                InputOutput.getComboBoxes()[0].setSelectedItem(selectedItem2);
                InputOutput.getComboBoxes()[1].setSelectedItem(selectedItem1);
            }
        });

        GUI.getAppWindow().add(swapLblBtn);
    }

    /*
     * Erstellt einen "Kopier" Knopf
     * 
     * Das Ergebnis wird für den Nutzer in das Clipboard gespeichert
     */
    private static void addCopyOutputLbl() {
        /*
         * Nimmt das originale .png und skaliert es runter zu der angegebenen Auflösung
         * Scale_Smooth hinterlässt dem Bild einen AA (Anti Aliasing) Effekt
         */
        ImageIcon originalIcon = new ImageIcon(GUI.class.getResource("/resources/buttons/button_copy.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        clipboardLblBtn.setIcon(scaledIcon);
        clipboardLblBtn.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClickAnimation.runCustomClickAnimation(clipboardLblBtn, () -> {

                    if (InputOutput.getTargetCur() != null) {
                        Utils.copyToClipboard();
                        runCustomFadeLabel("Kopiert!", clipboardLblBtn.getX() + 50, clipboardLblBtn.getY(), 70, 25);
                    } else {
                        PopupDisplay.throwErrorPopup(Language.getLangStringByKey("error_copy"));
                    }
                });
            }
        });

        GUI.getAppWindow().add(clipboardLblBtn);
    }

    /*
     * Diese Methode erstellt ein Label, dass dem Nutzer zurückgibt,
     * dass die eingegebenen Daten gespeichert sind.
     */
    public static void runCustomFadeLabel(String text, int locactionX, int locactionY, int width, int height) {
        fadeLbl.setText(text);
        fadeLbl.setBounds(locactionX, locactionY, width, height);

        fadeLbl.setVisible(true);
        Timer timer = new Timer(50, new ActionListener() {
            private float opacity = 1.0f; // opacity = transparenz

            /*
             * Nachdem der User die Daten abgespeichert hat,
             * erscheint das Label für ein paar Sekunden
             * und verschwindet wieder.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Die opacity wird mit jedem Durchlauf verringert
                // ..erreicht sie 0,00, so wird das Label "entfernt"
                opacity -= 0.02f;
                if (opacity <= 0.0f) {
                    ((Timer) e.getSource()).stop();
                    fadeLbl.setVisible(false);
                }
            }
        });
        timer.start();
    }

    private static void addFooter() {
        authorLbl.setText(CurrencyCalculator.getAppVersion() + "  by Leon, Jonas, Ewin");
        authorLbl.setForeground(Color.GRAY);

        GUI.getAppWindow().add(authorLbl);
    }

    /*
     * Diese Methode erstellt einen Knopf, um eingegebene Daten zu speichern
     */
    private static void addSaveCalculationButton() {
        saveBtn.setBounds(50, 450, 100, 25);
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (InputOutput.getInputVal() == null || InputOutput.getBaseCur() == null
                                || InputOutput.getTargetCur() == null) {
                            PopupDisplay.throwErrorPopup(
                                    Language.getLangStringByKey("error_lastcalc_isnull"));
                        } else {
                            LastCalculation.setConfigLastCalc(InputOutput.getBaseCur(), InputOutput.getTargetCur(),
                                    InputOutput.getInputVal());
                            runCustomFadeLabel(Language.getLangStringByKey("fadeLabel"), 200, 450, 100, 25);
                        }
                    }
                });
            }
        });

        GUI.getAppWindow().add(saveBtn);
    }

    /*
     * Diese Methode erstellt einen Knopf, um gespeicherte Daten zu laden
     */
    private static void addLoadLastCalculationButton() {
        loadBtn.addActionListener((e) -> {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    String[] lastCalcEntries = LastCalculation.getConfigLastCalc();
                    // Es löscht die "" im String
                    lastCalcEntries[0] = lastCalcEntries[0].replace("\"", "");
                    lastCalcEntries[1] = lastCalcEntries[1].replace("\"", "");
                    lastCalcEntries[2] = lastCalcEntries[2].replace("\"", "");
                    // Setzt die gespeicherten Daten in die Variable ein
                    InputOutput.setBaseCur(lastCalcEntries[0]);
                    InputOutput.setTargetCur(lastCalcEntries[1]);
                    InputOutput.setInputVal(lastCalcEntries[2]);

                    InputOutput.setInputValRes();
                    Calculations.runThreadedCalculation();
                }
            });
        });
        GUI.getAppWindow().add(loadBtn);
    }

    private static void addPresetLbl() {
        GUI.getAppWindow().add(presetLbl);
    }

    private static void addCustomFadeLbl() {
        fadeLbl.setVisible(false);

        GUI.getAppWindow().add(fadeLbl);
    }

    public static JLabel getPresetLbl() {
        return presetLbl;
    }

    public static JButton getSaveBtn() {
        return saveBtn;
    }

    public static JButton getLoadBtn() {
        return loadBtn;
    }

    public static JLabel getFadeLbl() {
        return fadeLbl;
    }

}
