//Carson FitzGibbon
//Team 4206 - The RoboVikes

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;

public class PowerUpSim extends JPanel implements ActionListener {
    //all of my GUI
    private JButton rScale, bScale, rSwitch1, bSwitch1, rSwitch2, bSwitch2, toggleTime,
                    btnRForce, btnBForce, btnRBoost, btnBBoost, btnRLevitate, btnBLevitate, playback;
    private JLabel lblBlu, lblRed, rForce, rLevitate, rBoost, bForce, bLevitate, bBoost, rCubes, bCubes, rSwitch1Cubes,
                   rSwitch2Cubes, bSwitch1Cubes, bSwitch2Cubes, rScaleCubes, bScaleCubes, rRate, bRate, rAdvantage, bAdvantage;
    private JTextField bScore, rScore, gameTime, rPath, bPath, r2Path, b2Path, r3Path, b3Path, timeMult;
    private JComboBox bClimb, rClimb;
    private JCheckBox cbxBLevitate, cbxRLevitate, cbxRBoost, cbxBBoost, cbxRForce, cbxBForce;
    private Color rOn, rOff, bOn, bOff, powerQueued, powerOn, powerOff;

    //my variables
    private boolean timeToggled, rPlaying, bPlaying;
    private int switch1, switch2, scale, rCubeAmt, bCubeAmt, matchTime, rPts, bPts;
    String[] comboVals, comboVals2;
    String[][] rCommands, bCommands, r2Commands, b2Commands, r3Commands, b3Commands;
    ArrayList powerQueue = new ArrayList(4);

    Thread timingThread;

    public PowerUpSim() {
        //set primitive values
        //switches/scales:
            //default is 0, 1 is red, -1 is blue (because i dont like strings!)
        switch1 = 0;
        switch2 = 0;
        scale = 0;
        rCubeAmt = 30;
        bCubeAmt = 30;
        matchTime = 0;

        timingThread = new Thread("Timing") {
            public void run() {
                int powerTime = 10;
                boolean autoSwap = true;
                while (timingThread.isAlive()) {
                    if (timeToggled & matchTime < 150) {
                        long time = System.currentTimeMillis();
                        int rRateTemp = 0, bRateTemp = 0;

                        if (matchTime > 15 & autoSwap) {
                            cbxBLevitate.setEnabled(true);
                            cbxRLevitate.setEnabled(true);
                            cbxBBoost.setEnabled(true);
                            cbxRBoost.setEnabled(true);
                            cbxBForce.setEnabled(true);
                            cbxRForce.setEnabled(true);

                            btnBLevitate.setEnabled(true);
                            btnRLevitate.setEnabled(true);
                            btnBBoost.setEnabled(true);
                            btnRBoost.setEnabled(true);
                            btnBForce.setEnabled(true);
                            btnRForce.setEnabled(true);

                            autoSwap = false;
                        }

                        if (matchTime > 120) {
                            rClimb.setEnabled(true);
                            bClimb.setEnabled(true);
                        }

                        if (powerTime == 0) {
                            powerTime = 10;
                            powerQueue.remove(0);
                            bSwitch1.setBackground(bOff);
                            rSwitch1.setBackground(rOff);
                            bSwitch2.setBackground(bOff);
                            rSwitch2.setBackground(rOff);
                            bScale.setBackground(bOff);
                            rScale.setBackground(rOff);
                        }

                        if (Integer.parseInt(rSwitch2Cubes.getText()) < Integer.parseInt(bSwitch2Cubes.getText())) {
                            bSwitch2.setBackground(bOn);
                            rSwitch2.setBackground(rOff);
                            switch2 = -1;
                        } else if (Integer.parseInt(rSwitch2Cubes.getText()) > Integer.parseInt(bSwitch2Cubes.getText())) {
                            bSwitch2.setBackground(bOff);
                            rSwitch2.setBackground(rOn);
                            switch2 = 1;
                        } else switch2 = 0;

                        if (Integer.parseInt(rSwitch1Cubes.getText()) < Integer.parseInt(bSwitch1Cubes.getText())) {
                            bSwitch1.setBackground(bOn);
                            rSwitch1.setBackground(rOff);
                            switch1 = -1;
                        } else if (Integer.parseInt(rSwitch1Cubes.getText()) > Integer.parseInt(bSwitch1Cubes.getText())) {
                            bSwitch1.setBackground(bOff);
                            rSwitch1.setBackground(rOn);
                            switch1 = 1;
                        } else switch1 = 0;

                        if (Integer.parseInt(rScaleCubes.getText()) > Integer.parseInt(bScaleCubes.getText())) {
                            bScale.setBackground(bOff);
                            rScale.setBackground(rOn);
                            scale = 1;
                        } else if (Integer.parseInt(rScaleCubes.getText()) < Integer.parseInt(bScaleCubes.getText())) {
                            bScale.setBackground(bOn);
                            rScale.setBackground(rOff);
                            scale = -1;
                        } else scale = 0;

                        if (powerQueue.isEmpty()) {
                            if (switch1 == 0) {
                                bSwitch1.setBackground(bOff);
                                rSwitch1.setBackground(rOff);
                            }
                            if (switch1 == 1) {
                                bSwitch1.setBackground(bOff);
                                rSwitch1.setBackground(rOn);
                            }
                            if (switch1 == -1) {
                                bSwitch1.setBackground(bOn);
                                rSwitch1.setBackground(rOff);
                            }

                            if (switch2 == 0) {
                                bSwitch2.setBackground(bOff);
                                rSwitch2.setBackground(rOff);
                            }
                            if (switch2 == 1) {
                                bSwitch2.setBackground(bOff);
                                rSwitch2.setBackground(rOn);
                            }
                            if (switch2 == -1) {
                                bSwitch2.setBackground(bOn);
                                rSwitch2.setBackground(rOff);
                            }

                            if (scale == 0) {
                                bScale.setBackground(bOff);
                                rScale.setBackground(rOff);
                            }
                            if (scale == 1) {
                                bScale.setBackground(bOff);
                                rScale.setBackground(rOn);
                            }
                            if (scale == -1) {
                                bScale.setBackground(bOn);
                                rScale.setBackground(rOff);
                            }
                        }

                        if (!powerQueue.isEmpty()) {
                            if (btnBBoost.getBackground() == powerOn & !(powerQueue.get(0).equals("bBoost1") | powerQueue.get(0).equals("bBoost2") | powerQueue.get(0).equals("bBoost3"))) {
                                btnBBoost.setBackground(powerOff);
                                btnBBoost.setForeground(Color.WHITE);
                            } else if (btnBBoost.getBackground() == powerQueued & (powerQueue.get(0).equals("bBoost1") | powerQueue.get(0).equals("bBoost2") | powerQueue.get(0).equals("bBoost3"))) {
                                btnBBoost.setBackground(powerOn);
                            }

                            if (btnRBoost.getBackground() == powerOn & !(powerQueue.get(0).equals("rBoost1") | powerQueue.get(0).equals("rBoost2") | powerQueue.get(0).equals("rBoost3"))) {
                                btnRBoost.setBackground(powerOff);
                                btnRBoost.setForeground(Color.WHITE);
                            } else if (btnRBoost.getBackground() == powerQueued & (powerQueue.get(0).equals("rBoost1") | powerQueue.get(0).equals("rBoost2") | powerQueue.get(0).equals("rBoost3"))) {
                                btnRBoost.setBackground(powerOn);
                            }

                            if (btnBForce.getBackground() == powerOn & !(powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                                btnBForce.setBackground(powerOff);
                                btnBForce.setForeground(Color.WHITE);
                            } else if (btnBForce.getBackground() == powerQueued & (powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                                btnBForce.setBackground(powerOn);
                            }

                            if (btnRForce.getBackground() == powerOn & !(powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                                btnRForce.setBackground(powerOff);
                                btnRForce.setForeground(Color.WHITE);
                            } else if (btnRForce.getBackground() == powerQueued & (powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                                btnRForce.setBackground(powerOn);
                            }
                        } else {
                            if (btnBBoost.getBackground() == powerOn) {
                                btnBBoost.setBackground(powerOff);
                                btnBBoost.setForeground(Color.WHITE);
                            }

                            if (btnRBoost.getBackground() == powerOn) {
                                btnRBoost.setBackground(powerOff);
                                btnRBoost.setForeground(Color.WHITE);
                            }

                            if (btnBForce.getBackground() == powerOn) {
                                btnBForce.setBackground(powerOff);
                                btnBForce.setForeground(Color.WHITE);
                            }

                            if (btnRForce.getBackground() == powerOn) {
                                btnRForce.setBackground(powerOff);
                                btnRForce.setForeground(Color.WHITE);
                            }
                        }

                        if (rPlaying|bPlaying) { //if we ARE in playback
                            String[] rInstanceCommands = new String[7];
                            String[] bInstanceCommands = new String[7];
                            String[] r2InstanceCommands = new String[7];
                            String[] b2InstanceCommands = new String[7];
                            String[] r3InstanceCommands = new String[7];
                            String[] b3InstanceCommands = new String[7];
                            for (int i=0; i<7; i++) {
                                if (rPlaying) {
                                    rInstanceCommands[i] = rCommands[matchTime][i];
                                    r2InstanceCommands[i] = r2Commands[matchTime][i];
                                    r3InstanceCommands[i] = r3Commands[matchTime][i];
                                }
                                if (bPlaying) {
                                    bInstanceCommands[i] = bCommands[matchTime][i];
                                    b2InstanceCommands[i] = b2Commands[matchTime][i];
                                    b3InstanceCommands[i] = b3Commands[matchTime][i];
                                }
                            }
                            /* Command list:
                            +boost
                            +levitate
                            +force
                            boost
                            levitate
                            force
                            mySwitch
                            oppSwitch
                            scale
                            +climb

                            all commands are relative to the alliance calling them, so "mySwitch" in the red file adds
                            a block to the red side's red switch plate since you won't ever put blocks on the blu plate
                             */

                            for (int i=0; i<7; i++) {
                                if (rInstanceCommands[i] != null) {
                                    switch (rInstanceCommands[i].trim()) {
                                        case "scale":
                                            if (rCubeAmt > 0) {
                                                rScaleCubes.setText(String.valueOf(Integer.parseInt(rScaleCubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rScaleCubes.getText()) > Integer.parseInt(bScaleCubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bScale.setBackground(bOff);
                                                        rScale.setBackground(rOn);
                                                        if (scale != 1) {
                                                            rPts++;
                                                            if (matchTime <= 15) rPts++;
                                                        }
                                                        scale = 1;
                                                    } else if (!(powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                                                        bScale.setBackground(bOff);
                                                        rScale.setBackground(rOn);
                                                        if (scale != 1) {
                                                            rPts++;
                                                            if (matchTime <= 15) rPts++;
                                                        }
                                                        scale = 1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "oppSwitch":
                                            if (rCubeAmt > 0) {
                                                rSwitch1Cubes.setText(String.valueOf(Integer.parseInt(rSwitch1Cubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rSwitch1Cubes.getText()) > Integer.parseInt(bSwitch1Cubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bSwitch1.setBackground(bOff);
                                                        rSwitch1.setBackground(rOn);
                                                        switch1 = 1;
                                                    } else if (!(powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce3"))) {
                                                        bSwitch1.setBackground(bOff);
                                                        rSwitch1.setBackground(rOn);
                                                        switch1 = 1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "mySwitch":
                                            if (rCubeAmt > 0) {
                                                rSwitch2Cubes.setText(String.valueOf(Integer.parseInt(rSwitch2Cubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rSwitch2Cubes.getText()) > Integer.parseInt(bSwitch2Cubes.getText())) {
                                                    bSwitch2.setBackground(bOff);
                                                    rSwitch2.setBackground(rOn);
                                                    if (switch2 != 1) {
                                                        rPts++;
                                                        if (matchTime <= 15) rPts++;
                                                    }
                                                    switch2 = 1;
                                                }
                                            }
                                            break;

                                        case "+force":
                                            if (rCubeAmt > 0 & !btnRForce.getText().equals("3")) {
                                                btnRForce.setText(String.valueOf(Integer.parseInt(btnRForce.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+boost":
                                            if (rCubeAmt > 0 & !btnRBoost.getText().equals("3")) {
                                                btnRBoost.setText(String.valueOf(Integer.parseInt(btnRBoost.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+levitate":
                                            if (rCubeAmt > 0 & !btnRLevitate.getText().equals("3")) {
                                                btnRLevitate.setText(String.valueOf(Integer.parseInt(btnRLevitate.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+climb":
                                            if (rClimb.getSelectedIndex()<3) rClimb.setSelectedIndex(rClimb.getSelectedIndex()+1);
                                    }
                                }

                                if (r2InstanceCommands[i] != null) {
                                    switch (r2InstanceCommands[i].trim()) {
                                        case "scale":
                                            if (rCubeAmt > 0) {
                                                rScaleCubes.setText(String.valueOf(Integer.parseInt(rScaleCubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rScaleCubes.getText()) > Integer.parseInt(bScaleCubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bScale.setBackground(bOff);
                                                        rScale.setBackground(rOn);
                                                        if (scale != 1) {
                                                            rPts++;
                                                            if (matchTime <= 15) rPts++;
                                                        }
                                                        scale = 1;
                                                    } else if (!(powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                                                        bScale.setBackground(bOff);
                                                        rScale.setBackground(rOn);
                                                        if (scale != 1) {
                                                            rPts++;
                                                            if (matchTime <= 15) rPts++;
                                                        }
                                                        scale = 1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "oppSwitch":
                                            if (rCubeAmt > 0) {
                                                rSwitch1Cubes.setText(String.valueOf(Integer.parseInt(rSwitch1Cubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rSwitch1Cubes.getText()) > Integer.parseInt(bSwitch1Cubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bSwitch1.setBackground(bOff);
                                                        rSwitch1.setBackground(rOn);
                                                        switch1 = 1;
                                                    } else if (!(powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce3"))) {
                                                        bSwitch1.setBackground(bOff);
                                                        rSwitch1.setBackground(rOn);
                                                        switch1 = 1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "mySwitch":
                                            if (rCubeAmt > 0) {
                                                rSwitch2Cubes.setText(String.valueOf(Integer.parseInt(rSwitch2Cubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rSwitch2Cubes.getText()) > Integer.parseInt(bSwitch2Cubes.getText())) {
                                                    bSwitch2.setBackground(bOff);
                                                    rSwitch2.setBackground(rOn);
                                                    if (switch2 != 1) {
                                                        rPts++;
                                                        if (matchTime <= 15) rPts++;
                                                    }
                                                    switch2 = 1;
                                                }
                                            }
                                            break;

                                        case "+force":
                                            if (rCubeAmt > 0 & !btnRForce.getText().equals("3")) {
                                                btnRForce.setText(String.valueOf(Integer.parseInt(btnRForce.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+boost":
                                            if (rCubeAmt > 0 & !btnRBoost.getText().equals("3")) {
                                                btnRBoost.setText(String.valueOf(Integer.parseInt(btnRBoost.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+levitate":
                                            if (rCubeAmt > 0 & !btnRLevitate.getText().equals("3")) {
                                                btnRLevitate.setText(String.valueOf(Integer.parseInt(btnRLevitate.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+climb":
                                            if (rClimb.getSelectedIndex()<3) rClimb.setSelectedIndex(rClimb.getSelectedIndex()+1);
                                    }
                                }

                                if (r3InstanceCommands[i] != null) {
                                    switch (r3InstanceCommands[i].trim()) {
                                        case "scale":
                                            if (rCubeAmt > 0) {
                                                rScaleCubes.setText(String.valueOf(Integer.parseInt(rScaleCubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rScaleCubes.getText()) > Integer.parseInt(bScaleCubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bScale.setBackground(bOff);
                                                        rScale.setBackground(rOn);
                                                        if (scale != 1) {
                                                            rPts++;
                                                            if (matchTime <= 15) rPts++;
                                                        }
                                                        scale = 1;
                                                    } else if (!(powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                                                        bScale.setBackground(bOff);
                                                        rScale.setBackground(rOn);
                                                        if (scale != 1) {
                                                            rPts++;
                                                            if (matchTime <= 15) rPts++;
                                                        }
                                                        scale = 1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "oppSwitch":
                                            if (rCubeAmt > 0) {
                                                rSwitch1Cubes.setText(String.valueOf(Integer.parseInt(rSwitch1Cubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rSwitch1Cubes.getText()) > Integer.parseInt(bSwitch1Cubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bSwitch1.setBackground(bOff);
                                                        rSwitch1.setBackground(rOn);
                                                        switch1 = 1;
                                                    } else if (!(powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce3"))) {
                                                        bSwitch1.setBackground(bOff);
                                                        rSwitch1.setBackground(rOn);
                                                        switch1 = 1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "mySwitch":
                                            if (rCubeAmt > 0) {
                                                rSwitch2Cubes.setText(String.valueOf(Integer.parseInt(rSwitch2Cubes.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                if (Integer.parseInt(rSwitch2Cubes.getText()) > Integer.parseInt(bSwitch2Cubes.getText())) {
                                                    bSwitch2.setBackground(bOff);
                                                    rSwitch2.setBackground(rOn);
                                                    if (switch2 != 1) {
                                                        rPts++;
                                                        if (matchTime <= 15) rPts++;
                                                    }
                                                    switch2 = 1;
                                                }
                                            }
                                            break;

                                        case "+force":
                                            if (rCubeAmt > 0 & !btnRForce.getText().equals("3")) {
                                                btnRForce.setText(String.valueOf(Integer.parseInt(btnRForce.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+boost":
                                            if (rCubeAmt > 0 & !btnRBoost.getText().equals("3")) {
                                                btnRBoost.setText(String.valueOf(Integer.parseInt(btnRBoost.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+levitate":
                                            if (rCubeAmt > 0 & !btnRLevitate.getText().equals("3")) {
                                                btnRLevitate.setText(String.valueOf(Integer.parseInt(btnRLevitate.getText()) + 1));
                                                rCubeAmt--;
                                                rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                                                rPts += 5;
                                            }
                                            break;

                                        case "+climb":
                                            if (rClimb.getSelectedIndex()<3) rClimb.setSelectedIndex(rClimb.getSelectedIndex()+1);
                                    }
                                }

                                if (bInstanceCommands[i] != null) {
                                    switch (bInstanceCommands[i].trim()) {
                                        case "scale":
                                            if (bCubeAmt > 0) {
                                                bScaleCubes.setText(String.valueOf(Integer.parseInt(bScaleCubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(bScaleCubes.getText()) > Integer.parseInt(rScaleCubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bScale.setBackground(bOn);
                                                        rScale.setBackground(rOff);
                                                        if (scale != -1) {
                                                            bPts++;
                                                            if (matchTime <= 15) bPts++;
                                                        }
                                                        scale = -1;
                                                    } else if (!(powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                                                        rScale.setBackground(bOn);
                                                        bScale.setBackground(rOff);
                                                        if (scale != -1) {
                                                            bPts++;
                                                            if (matchTime <= 15) bPts++;
                                                        }
                                                        scale = -1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "oppSwitch":
                                            if (bCubeAmt > 0) {
                                                bSwitch2Cubes.setText(String.valueOf(Integer.parseInt(bSwitch2Cubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(rSwitch2Cubes.getText()) < Integer.parseInt(bSwitch2Cubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bSwitch2.setBackground(bOn);
                                                        rSwitch2.setBackground(rOff);
                                                        switch2 = -1;
                                                    } else if (!(powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce3"))) {
                                                        bSwitch2.setBackground(bOn);
                                                        rSwitch2.setBackground(rOff);
                                                        switch2 = -1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "mySwitch":
                                            if (bCubeAmt > 0) {
                                                bSwitch1Cubes.setText(String.valueOf(Integer.parseInt(bSwitch1Cubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(rSwitch1Cubes.getText()) < Integer.parseInt(bSwitch1Cubes.getText())) {
                                                    bSwitch2.setBackground(bOn);
                                                    rSwitch2.setBackground(rOff);
                                                    if (switch1 != -1) {
                                                        bPts++;
                                                        if (matchTime <= 15) bPts++;
                                                    }
                                                    switch1 = -1;
                                                }
                                            }
                                            break;

                                        case "+force":
                                            if (bCubeAmt > 0 & !btnBForce.getText().equals("3")) {
                                                btnBForce.setText(String.valueOf(Integer.parseInt(btnBForce.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+boost":
                                            if (bCubeAmt > 0 & !btnBBoost.getText().equals("3")) {
                                                btnBBoost.setText(String.valueOf(Integer.parseInt(btnBBoost.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+levitate":
                                            if (bCubeAmt > 0 & !btnBLevitate.getText().equals("3")) {
                                                btnBLevitate.setText(String.valueOf(Integer.parseInt(btnBLevitate.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+climb":
                                            if (bClimb.getSelectedIndex()<3) bClimb.setSelectedIndex(bClimb.getSelectedIndex()+1);
                                    }
                                }

                                if (b2InstanceCommands[i] != null) {
                                    switch (b2InstanceCommands[i].trim()) {
                                        case "scale":
                                            if (bCubeAmt > 0) {
                                                bScaleCubes.setText(String.valueOf(Integer.parseInt(bScaleCubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(bScaleCubes.getText()) > Integer.parseInt(rScaleCubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bScale.setBackground(bOn);
                                                        rScale.setBackground(rOff);
                                                        if (scale != -1) {
                                                            bPts++;
                                                            if (matchTime <= 15) bPts++;
                                                        }
                                                        scale = -1;
                                                    } else if (!(powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                                                        rScale.setBackground(bOn);
                                                        bScale.setBackground(rOff);
                                                        if (scale != -1) {
                                                            bPts++;
                                                            if (matchTime <= 15) bPts++;
                                                        }
                                                        scale = -1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "oppSwitch":
                                            if (bCubeAmt > 0) {
                                                bSwitch2Cubes.setText(String.valueOf(Integer.parseInt(bSwitch2Cubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(rSwitch2Cubes.getText()) < Integer.parseInt(bSwitch2Cubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bSwitch2.setBackground(bOn);
                                                        rSwitch2.setBackground(rOff);
                                                        switch2 = -1;
                                                    } else if (!(powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce3"))) {
                                                        bSwitch2.setBackground(bOn);
                                                        rSwitch2.setBackground(rOff);
                                                        switch2 = -1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "mySwitch":
                                            if (bCubeAmt > 0) {
                                                bSwitch1Cubes.setText(String.valueOf(Integer.parseInt(bSwitch1Cubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(rSwitch1Cubes.getText()) < Integer.parseInt(bSwitch1Cubes.getText())) {
                                                    bSwitch2.setBackground(bOn);
                                                    rSwitch2.setBackground(rOff);
                                                    if (switch1 != -1) {
                                                        bPts++;
                                                        if (matchTime <= 15) bPts++;
                                                    }
                                                    switch1 = -1;
                                                }
                                            }
                                            break;

                                        case "+force":
                                            if (bCubeAmt > 0 & !btnBForce.getText().equals("3")) {
                                                btnBForce.setText(String.valueOf(Integer.parseInt(btnBForce.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+boost":
                                            if (bCubeAmt > 0 & !btnBBoost.getText().equals("3")) {
                                                btnBBoost.setText(String.valueOf(Integer.parseInt(btnBBoost.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+levitate":
                                            if (bCubeAmt > 0 & !btnBLevitate.getText().equals("3")) {
                                                btnBLevitate.setText(String.valueOf(Integer.parseInt(btnBLevitate.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+climb":
                                            if (bClimb.getSelectedIndex()<3) bClimb.setSelectedIndex(bClimb.getSelectedIndex()+1);
                                    }
                                }

                                if (b3InstanceCommands[i] != null) {
                                    switch (b3InstanceCommands[i].trim()) {
                                        case "scale":
                                            if (bCubeAmt > 0) {
                                                bScaleCubes.setText(String.valueOf(Integer.parseInt(bScaleCubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(bScaleCubes.getText()) > Integer.parseInt(rScaleCubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bScale.setBackground(bOn);
                                                        rScale.setBackground(rOff);
                                                        if (scale != -1) {
                                                            bPts++;
                                                            if (matchTime <= 15) bPts++;
                                                        }
                                                        scale = -1;
                                                    } else if (!(powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                                                        rScale.setBackground(bOn);
                                                        bScale.setBackground(rOff);
                                                        if (scale != -1) {
                                                            bPts++;
                                                            if (matchTime <= 15) bPts++;
                                                        }
                                                        scale = -1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "oppSwitch":
                                            if (bCubeAmt > 0) {
                                                bSwitch2Cubes.setText(String.valueOf(Integer.parseInt(bSwitch2Cubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(rSwitch2Cubes.getText()) < Integer.parseInt(bSwitch2Cubes.getText())) {
                                                    if (powerQueue.isEmpty()) {
                                                        bSwitch2.setBackground(bOn);
                                                        rSwitch2.setBackground(rOff);
                                                        switch2 = -1;
                                                    } else if (!(powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce3"))) {
                                                        bSwitch2.setBackground(bOn);
                                                        rSwitch2.setBackground(rOff);
                                                        switch2 = -1;
                                                    }
                                                }
                                            }
                                            break;

                                        case "mySwitch":
                                            if (bCubeAmt > 0) {
                                                bSwitch1Cubes.setText(String.valueOf(Integer.parseInt(bSwitch1Cubes.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                if (Integer.parseInt(rSwitch1Cubes.getText()) < Integer.parseInt(bSwitch1Cubes.getText())) {
                                                    bSwitch2.setBackground(bOn);
                                                    rSwitch2.setBackground(rOff);
                                                    if (switch1 != -1) {
                                                        bPts++;
                                                        if (matchTime <= 15) bPts++;
                                                    }
                                                    switch1 = -1;
                                                }
                                            }
                                            break;

                                        case "+force":
                                            if (bCubeAmt > 0 & !btnBForce.getText().equals("3")) {
                                                btnBForce.setText(String.valueOf(Integer.parseInt(btnBForce.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+boost":
                                            if (bCubeAmt > 0 & !btnBBoost.getText().equals("3")) {
                                                btnBBoost.setText(String.valueOf(Integer.parseInt(btnBBoost.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+levitate":
                                            if (bCubeAmt > 0 & !btnBLevitate.getText().equals("3")) {
                                                btnBLevitate.setText(String.valueOf(Integer.parseInt(btnBLevitate.getText()) + 1));
                                                bCubeAmt--;
                                                bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                                                bPts += 5;
                                            }
                                            break;

                                        case "+climb":
                                            if (bClimb.getSelectedIndex()<3) bClimb.setSelectedIndex(bClimb.getSelectedIndex()+1);
                                    }
                                }
                            }
                        }

                        if (!powerQueue.isEmpty()) {
                            if ((powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce3")) & switch2 != 1) {
                                bSwitch2.setBackground(bOff);
                                rSwitch2.setBackground(rOn);
                                rPts++;
                                rRateTemp++;
                                btnRForce.setBackground(powerOn);
                            } else if (switch2 == 1) {
                                try {
                                    if (powerQueue.get(0).equals("rBoost3") | powerQueue.get(0).equals("rBoost1")) {
                                        rPts++;
                                        rRateTemp++;
                                        btnRBoost.setBackground(powerOn);
                                    }
                                } catch (IndexOutOfBoundsException ex) {

                                }
                                rPts++;
                                rRateTemp++;
                                bSwitch2.setBackground(bOff);
                                rSwitch2.setBackground(rOn);
                            }
                        } else if (switch2 == 1) {
                            rPts++;
                            rRateTemp++;
                            if (matchTime <= 15) {
                                rPts++;
                                rRateTemp++;
                            }
                            bSwitch2.setBackground(bOff);
                            rSwitch2.setBackground(rOn);
                        }

                        if (!powerQueue.isEmpty()) {
                            if ((powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce3")) & switch1 != -1) {
                                bSwitch1.setBackground(bOn);
                                rSwitch1.setBackground(rOff);
                                bPts++;
                                bRateTemp++;
                                btnBForce.setBackground(powerOn);
                            } else if (switch1 == -1) {
                                try {
                                    if (powerQueue.get(0).equals("bBoost3") | powerQueue.get(0).equals("bBoost1")) {
                                        bPts++;
                                        bRateTemp++;
                                        btnBBoost.setBackground(powerOn);
                                    }
                                } catch (IndexOutOfBoundsException ex) {

                                }
                                bPts++;
                                bRateTemp++;
                                bSwitch1.setBackground(bOn);
                                rSwitch1.setBackground(rOff);
                            }
                        } else if (switch1 == -1) {
                            bPts++;
                            bRateTemp++;
                            bSwitch1.setBackground(bOn);
                            rSwitch1.setBackground(rOff);
                            if (matchTime <= 15) {
                                bPts++;
                                bRateTemp++;
                            }
                        }

                        if (!powerQueue.isEmpty()) {
                            if ((powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3")) & scale != 1) {
                                bScale.setBackground(bOff);
                                rScale.setBackground(rOn);
                                rPts++;
                                rRateTemp++;
                                btnRForce.setBackground(powerOn);
                            } else if (scale == 1 & !(powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                                try {
                                    if (powerQueue.get(0).equals("bBoost3") | powerQueue.get(0).equals("rBoost3") | powerQueue.get(0).equals("bBoost2") | powerQueue.get(0).equals("rBoost2")) {
                                        rPts++;
                                        rRateTemp++;
                                        if (powerQueue.get(0).equals("rBoost3") | powerQueue.get(0).equals("rBoost2"))
                                            btnRBoost.setBackground(powerOn);
                                    }
                                } catch (IndexOutOfBoundsException ex) {

                                }
                                rPts++;
                                rRateTemp++;
                                bScale.setBackground(bOff);
                                rScale.setBackground(rOn);
                            }
                        } else if (scale == 1) {
                            rPts++;
                            rRateTemp++;
                            bScale.setBackground(bOff);
                            rScale.setBackground(rOn);
                            if (matchTime <= 15) {
                                rPts++;
                                rRateTemp++;
                            }
                        }

                        if (!powerQueue.isEmpty()) {
                            if ((powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3")) & scale != -1) {
                                bScale.setBackground(bOn);
                                rScale.setBackground(rOff);
                                bPts++;
                                bRateTemp++;
                                btnBForce.setBackground(powerOn);
                            } else if (scale == -1 & !(powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                                try {
                                    if (powerQueue.get(0).equals("bBoost3") | powerQueue.get(0).equals("rBoost3") | powerQueue.get(0).equals("bBoost2") | powerQueue.get(0).equals("rBoost2"))
                                        bPts++;
                                    bRateTemp++;
                                    if (powerQueue.get(0).equals("bBoost3") | powerQueue.get(0).equals("bBoost2"))
                                        btnBBoost.setBackground(powerOn);
                                } catch (IndexOutOfBoundsException ex) {

                                }
                                bPts++;
                                bRateTemp++;
                                bScale.setBackground(bOn);
                                rScale.setBackground(rOff);
                            }
                        } else if (scale == -1) {
                            bPts++;
                            bRateTemp++;
                            bScale.setBackground(bOn);
                            rScale.setBackground(rOff);
                            if (matchTime <= 15) {
                                bPts++;
                                bRateTemp++;
                            }
                        }

                        try {
                            while (System.currentTimeMillis() < time + (1000 / Integer.parseInt(timeMult.getText()))) {

                            }
                        } catch (NumberFormatException ex) {
                            while (System.currentTimeMillis() < time + (1000)) {

                            }
                        }
                        matchTime++;

                        rScore.setText(String.valueOf(rPts));
                        bScore.setText(String.valueOf(bPts));
                        rRate.setText(String.valueOf(rRateTemp));
                        bRate.setText(String.valueOf(bRateTemp));
                        try {
                            if (((rPts - bPts) / (bRateTemp - rRateTemp)) >= 0 & ((rPts - bPts) / (bRateTemp - rRateTemp)) <= 150-matchTime) {
                                if (rPts < bPts) {
                                    rAdvantage.setText("Red Advantage!");
                                    bAdvantage.setText("");
                                } else if (rPts > bPts) {
                                    rAdvantage.setText("");
                                    bAdvantage.setText("Blu Advantage!");
                                } else {
                                    rAdvantage.setText("");
                                    bAdvantage.setText("");
                                }
                            } else {
                                if (rPts > bPts) {
                                    rAdvantage.setText("Red Advantage!");
                                    bAdvantage.setText("");
                                } else if (rPts < bPts) {
                                    rAdvantage.setText("");
                                    bAdvantage.setText("Blu Advantage!");
                                } else {
                                    rAdvantage.setText("");
                                    bAdvantage.setText("");
                                }
                            }
                        } catch (ArithmeticException divZ) {
                            if (rPts > bPts) {
                                rAdvantage.setText("Red Advantage!");
                                bAdvantage.setText("");
                            } else if (rPts < bPts) {
                                rAdvantage.setText("");
                                bAdvantage.setText("Blu Advantage!");
                            } else {
                                rAdvantage.setText("");
                                bAdvantage.setText("");
                            }
                        }
                        if (!powerQueue.isEmpty()) powerTime--;
                        gameTime.setText(String.valueOf(matchTime));
                    }
                    if (matchTime >=150) break;
                }
                rPts += 30*rClimb.getSelectedIndex();
                bPts += 30*bClimb.getSelectedIndex();

                if (cbxRLevitate.isSelected() & rClimb.getSelectedIndex()<3) rPts+=30;
                if (cbxBLevitate.isSelected() & bClimb.getSelectedIndex()<3) bPts+=30;

                rScore.setText(String.valueOf(rPts));
                bScore.setText(String.valueOf(bPts));

                if (rPts > bPts) rScore.setBackground(Color.GREEN);
                else if (rPts < bPts) bScore.setBackground(Color.GREEN);
            }
        };

        //construct guivars
        comboVals = new String[]{"0", "1", "2", "3"};
        comboVals2 = new String[]{"0", "1", "2"};
        rOff = new Color(175,0,0);
        rOn = new Color(255,0,0);
        bOff = new Color(0,0,125);
        bOn = new Color(0,0,255);
        powerQueued = new Color(190,170,0);
        powerOn = new Color(0,150,0);
        powerOff = new Color(0,0,0);

        //construct gui
        timeMult = new JTextField("1");
        rPath = new JTextField("Enter the red alliance's CSV file name here!");
        bPath = new JTextField("Enter the blue alliance's CSV file name here!");
        r3Path = new JTextField("Enter the red alliance's CSV file name here!");
        b3Path = new JTextField("Enter the blue alliance's CSV file name here!");
        r2Path = new JTextField("Enter the red alliance's CSV file name here!");
        b2Path = new JTextField("Enter the blue alliance's CSV file name here!");
        playback = new JButton("Play");
        rScale = new JButton ("Red Scale");
        bScale = new JButton ("Blu Scale");
        rSwitch1 = new JButton ("Red Switch");
        bSwitch1 = new JButton ("Blu Switch");
        rSwitch2 = new JButton ("Red Switch");
        bSwitch2 = new JButton ("Blu Switch");
        rCubes = new JLabel ("Cubes: 30");
        bCubes = new JLabel ("Cubes: 30");
        rScaleCubes = new JLabel ("0");
        bScaleCubes = new JLabel ("0");
        rSwitch1Cubes = new JLabel ("0");
        rSwitch2Cubes = new JLabel ("0");
        bSwitch1Cubes = new JLabel ("0");
        bSwitch2Cubes = new JLabel ("0");
        lblBlu = new JLabel ("Blue Alliance");
        lblRed = new JLabel ("Red Alliance");
        rForce = new JLabel ("Force");
        rLevitate = new JLabel ("Levitate");
        rBoost = new JLabel ("Boost");
        btnRForce = new JButton ("0");
        btnRBoost = new JButton ("0");
        bForce = new JLabel ("Force");
        btnBForce = new JButton ("0");
        btnBBoost = new JButton ("0");
        btnRLevitate = new JButton("0");
        btnBLevitate = new JButton("0");
        bLevitate = new JLabel ("Levitate");
        bBoost = new JLabel ("Boost");
        gameTime = new JTextField (5);
        bScore = new JTextField (5);
        rScore = new JTextField (5);
        bClimb = new JComboBox (comboVals);
        rClimb = new JComboBox (comboVals);
        cbxBLevitate = new JCheckBox ("");
        cbxRLevitate = new JCheckBox ("");
        cbxBForce = new JCheckBox ("");
        cbxRForce = new JCheckBox ("");
        cbxBBoost = new JCheckBox ("");
        cbxRBoost = new JCheckBox ("");
        toggleTime = new JButton ("Start Time");
        rRate = new JLabel("0");
        bRate = new JLabel("0");
        rAdvantage = new JLabel("");
        bAdvantage = new JLabel("");

        //adjust size and set layout
        setPreferredSize (new Dimension (675, 535));
        setLayout (null);

        //set components properties
        timeMult.setBounds(500, 265, 25, 25);
        rPath.setBounds(25, 325, 625, 25);
        bPath.setBounds(25, 410, 625, 25);
        r2Path.setBounds(25, 350, 625, 25);
        b2Path.setBounds(25, 435, 625, 25);
        r3Path.setBounds(25, 375, 625, 25);
        b3Path.setBounds(25, 460, 625, 25);
        playback.setBounds(35, 490, 100, 25);
            playback.setActionCommand("playback");
            playback.addActionListener(this);
        rScale.setBounds (290, 40, 100, 100);
            rScale.setBackground(rOff);
            rScale.setForeground(Color.BLACK);
            rScale.setFocusPainted(false);
            rScale.setActionCommand("rScale");
            rScale.addActionListener(this);
        bScale.setBounds (290, 140, 100, 100);
            bScale.setBackground(bOff);
            bScale.setForeground(Color.BLACK);
            bScale.setFocusPainted(false);
            bScale.setActionCommand("bScale");
            bScale.addActionListener(this);
        rSwitch1.setBounds (150, 90, 100, 50);
            rSwitch1.setBackground(rOff);
            rSwitch1.setForeground(Color.BLACK);
            rSwitch1.setFocusPainted(false);
            rSwitch1.setActionCommand("rSwitch1");
            rSwitch1.addActionListener(this);
        bSwitch1.setBounds (150, 140, 100, 50);
            bSwitch1.setBackground(bOff);
            bSwitch1.setForeground(Color.BLACK);
            bSwitch1.setFocusPainted(false);
            bSwitch1.setActionCommand("bSwitch1");
            bSwitch1.addActionListener(this);
        rSwitch2.setBounds (430, 90, 100, 50);
            rSwitch2.setBackground(rOff);
            rSwitch2.setForeground(Color.BLACK);
            rSwitch2.setFocusPainted(false);
            rSwitch2.setActionCommand("rSwitch2");
            rSwitch2.addActionListener(this);
        bSwitch2.setBounds (430, 140, 100, 50);
            bSwitch2.setBackground(bOff);
            bSwitch2.setForeground(Color.BLACK);
            bSwitch2.setFocusPainted(false);
            bSwitch2.setActionCommand("bSwitch2");
            bSwitch2.addActionListener(this);
        lblBlu.setBounds (90, 40, 100, 25);
        lblRed.setBounds (500, 40, 100, 25);
        rForce.setBounds (600, 70, 100, 25);
        rLevitate.setBounds (600, 95, 100, 25);
        rBoost.setBounds (600, 120, 100, 25);
        btnRForce.setBounds (545, 70, 45, 25);
            btnRForce.setForeground(Color.BLACK);
            btnRForce.setFocusPainted(false);
            btnRForce.setActionCommand("btnRForce");
            btnRForce.addActionListener(this);
        btnRBoost.setBounds (545, 120, 45, 25);
            btnRBoost.setForeground(Color.BLACK);
            btnRBoost.setFocusPainted(false);
            btnRBoost.setActionCommand("btnRBoost");
            btnRBoost.addActionListener(this);
        bForce.setBounds (40, 70, 100, 25);
        btnBForce.setBounds (90, 70, 45, 25);
            btnBForce.setForeground(Color.BLACK);
            btnBForce.setFocusPainted(false);
            btnBForce.setActionCommand("btnBForce");
            btnBForce.addActionListener(this);
        btnBBoost.setBounds (90, 120, 45, 25);
            btnBBoost.setForeground(Color.BLACK);
            btnBBoost.setFocusPainted(false);
            btnBBoost.setActionCommand("btnBBoost");
            btnBBoost.addActionListener(this);
        btnBLevitate.setBounds(90,95,45,25);
            btnBLevitate.setForeground(Color.BLACK);
            btnBLevitate.setFocusPainted(false);
            btnBLevitate.setActionCommand("btnBLevitate");
            btnBLevitate.addActionListener(this);
        btnRLevitate.setBounds(545,95,45,25);
            btnRLevitate.setForeground(Color.BLACK);
            btnRLevitate.setFocusPainted(false);
            btnRLevitate.setActionCommand("btnRLevitate");
            btnRLevitate.addActionListener(this);
        bLevitate.setBounds (40, 95, 100, 25);
        bBoost.setBounds (40, 120, 100, 25);
        gameTime.setBounds (290, 10, 100, 25);
            gameTime.setText("0");
            gameTime.setEnabled(false);
        bScore.setBounds (185, 20, 100, 25);
            bScore.setText("0");
            bScore.setEnabled(false);
        rScore.setBounds (395, 20, 100, 25);
            rScore.setText("0");
            rScore.setEnabled(false);
        bClimb.setBounds (250, 90, 40, 25);
        rClimb.setBounds (390, 90, 40, 25);
        bCubes.setBounds(40,145,100,25);
        rCubes.setBounds(545,145,100,25);
        bSwitch1Cubes.setBounds(150,190,40,25);
        rSwitch1Cubes.setBounds(150,65,40,25);
        bSwitch2Cubes.setBounds(500,190,40,25);
        rSwitch2Cubes.setBounds(500,65,40,25);
        rScaleCubes.setBounds(390,50,100,25);
        bScaleCubes.setBounds(390,215,100,25);
        cbxBLevitate.setBounds (15, 95, 25, 25);
            cbxBLevitate.setActionCommand("cbxBLevitate");
            cbxBLevitate.addActionListener(this);
        cbxRLevitate.setBounds (650, 95, 25, 25);
            cbxRLevitate.setActionCommand("cbxRLevitate");
            cbxRLevitate.addActionListener(this);
        cbxBBoost.setBounds (15, 120, 25, 25);
            cbxBBoost.setActionCommand("cbxBBoost");
            cbxBBoost.addActionListener(this);
        cbxRBoost.setBounds (650, 120, 25, 25);
            cbxRBoost.setActionCommand("cbxRBoost");
            cbxRBoost.addActionListener(this);
        cbxBForce.setBounds (15, 70, 25, 25);
            cbxBForce.setActionCommand("cbxBForce");
            cbxBForce.addActionListener(this);
        cbxRForce.setBounds (650, 70, 25, 25);
            cbxRForce.setActionCommand("cbxRForce");
            cbxRForce.addActionListener(this);
        toggleTime.setBounds (185, 255, 310, 50);
            toggleTime.setActionCommand("toggleTime");
            toggleTime.addActionListener(this);
        cbxBLevitate.setEnabled(false);
        cbxRLevitate.setEnabled(false);
        cbxBBoost.setEnabled(false);
        cbxRBoost.setEnabled(false);
        cbxBForce.setEnabled(false);
        cbxRForce.setEnabled(false);
        btnBLevitate.setEnabled(false);
        btnRLevitate.setEnabled(false);
        btnBBoost.setEnabled(false);
        btnRBoost.setEnabled(false);
        btnBForce.setEnabled(false);
        btnRForce.setEnabled(false);
        rClimb.setEnabled(false);
        bClimb.setEnabled(false);
        rRate.setBounds(545,240,100,25);
        bRate.setBounds(40,240,100,25);
        rAdvantage.setBounds(545,265,100,25);
        bAdvantage.setBounds(40,265,100,25);

        //add components
        add(rPath);
        add(bPath);
        add(r2Path);
        add(b2Path);
        add(r3Path);
        add(b3Path);
        add(playback);
        add (rScale);
        add (bScale);
        add (rSwitch1);
        add (bSwitch1);
        add (rSwitch2);
        add (bSwitch2);
        add (lblBlu);
        add (lblRed);
        add (rForce);
        add (rLevitate);
        add (rBoost);
        add (btnRForce);
        add (btnRBoost);
        add (bForce);
        add (btnBForce);
        add (btnBBoost);
        add (btnBLevitate);
        add (btnRLevitate);
        add (bLevitate);
        add (bBoost);
        add (gameTime);
        add (bScore);
        add (rScore);
        add (bClimb);
        add (rClimb);
        add (cbxBLevitate);
        add (cbxRLevitate);
        add (cbxBBoost);
        add (cbxRBoost);
        add (cbxBForce);
        add (cbxRForce);
        add (toggleTime);
        add (rCubes);
        add (bCubes);
        add (rSwitch1Cubes);
        add (bSwitch1Cubes);
        add (rSwitch2Cubes);
        add (bSwitch2Cubes);
        add (rScaleCubes);
        add (bScaleCubes);
        add (rRate);
        add (bRate);
        add (rAdvantage);
        add (bAdvantage);
        add (timeMult);

        timingThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (action.equals("toggleTime")) {
            if (!timeToggled) toggleTime.setText("Stop Time");
            else toggleTime.setText("Start Time");
            timeToggled = !timeToggled;
        }
        switch (action) {
            case "rScale":
                if (rCubeAmt > 0 & !rPlaying) {
                    rScaleCubes.setText(String.valueOf(Integer.parseInt(rScaleCubes.getText()) + 1));
                    rCubeAmt--;
                    rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                    if (Integer.parseInt(rScaleCubes.getText()) > Integer.parseInt(bScaleCubes.getText())) {
                        if (powerQueue.isEmpty()) {
                            bScale.setBackground(bOff);
                            rScale.setBackground(rOn);
                            if (scale != 1) {
                                rPts++;
                                if (matchTime <= 15) rPts++;
                            }
                            scale = 1;
                        } else if (!(powerQueue.get(0).equals("bForce2") | powerQueue.get(0).equals("bForce3"))) {
                            bScale.setBackground(bOff);
                            rScale.setBackground(rOn);
                            if (scale != 1) {
                                rPts++;
                                if (matchTime <= 15) rPts++;
                            }
                            scale = 1;
                        }
                    }
                }
                break;

            case "bScale":
                if (bCubeAmt > 0 & !bPlaying) {
                    bScaleCubes.setText(String.valueOf(Integer.parseInt(bScaleCubes.getText()) + 1));
                    bCubeAmt--;
                    bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                    if (Integer.parseInt(rScaleCubes.getText()) < Integer.parseInt(bScaleCubes.getText())) {
                        if (powerQueue.isEmpty()) {
                            bScale.setBackground(bOn);
                            rScale.setBackground(rOff);
                            if (scale != -1) {
                                bPts++;
                                if (matchTime <= 15) bPts++;
                            }
                            scale = -1;
                        } else if (!(powerQueue.get(0).equals("rForce2") | powerQueue.get(0).equals("rForce3"))) {
                            bScale.setBackground(bOn);
                            rScale.setBackground(rOff);
                            if (scale != -1) {
                                bPts++;
                                if (matchTime <= 15) bPts++;
                            }
                            scale = -1;
                        }
                    }
                }
                break;

            case "rSwitch1":
                if (rCubeAmt > 0 & !rPlaying) {
                    rSwitch1Cubes.setText(String.valueOf(Integer.parseInt(rSwitch1Cubes.getText()) + 1));
                    rCubeAmt--;
                    rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                    if (Integer.parseInt(rSwitch1Cubes.getText()) > Integer.parseInt(bSwitch1Cubes.getText())) {
                        if (powerQueue.isEmpty()) {
                            bSwitch1.setBackground(bOff);
                            rSwitch1.setBackground(rOn);
                            switch1 = 1;
                        } else if (!(powerQueue.get(0).equals("bForce1") | powerQueue.get(0).equals("bForce3"))) {
                            bSwitch1.setBackground(bOff);
                            rSwitch1.setBackground(rOn);
                            switch1 = 1;
                        }
                    }
                }
                break;

            case "bSwitch1":
                if (bCubeAmt > 0 & !bPlaying) {
                    bSwitch1Cubes.setText(String.valueOf(Integer.parseInt(bSwitch1Cubes.getText()) + 1));
                    bCubeAmt--;
                    bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                    if (Integer.parseInt(rSwitch1Cubes.getText()) < Integer.parseInt(bSwitch1Cubes.getText())) {
                        bSwitch1.setBackground(bOn);
                        rSwitch1.setBackground(rOff);
                        if (switch1 != -1) {
                            bPts++;
                            if (matchTime <= 15) bPts++;
                        }
                        switch1 = -1;
                    }
                }
                break;

            case "rSwitch2":
                if (rCubeAmt > 0 & !rPlaying) {
                    rSwitch2Cubes.setText(String.valueOf(Integer.parseInt(rSwitch2Cubes.getText()) + 1));
                    rCubeAmt--;
                    rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                    if (Integer.parseInt(rSwitch2Cubes.getText()) > Integer.parseInt(bSwitch2Cubes.getText())) {
                        bSwitch2.setBackground(bOff);
                        rSwitch2.setBackground(rOn);
                        if (switch2 != 1) {
                            rPts++;
                            if (matchTime <= 15) rPts++;
                        }
                        switch2 = 1;
                    }
                }
                break;

            case "bSwitch2":
                if (bCubeAmt > 0 & !bPlaying) {
                    bSwitch2Cubes.setText(String.valueOf(Integer.parseInt(bSwitch2Cubes.getText()) + 1));
                    bCubeAmt--;
                    bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                    if (Integer.parseInt(rSwitch2Cubes.getText()) < Integer.parseInt(bSwitch2Cubes.getText())) {
                        if (powerQueue.isEmpty()) {
                            bSwitch2.setBackground(bOn);
                            rSwitch2.setBackground(rOff);
                            switch2 = -1;
                        } else if (!(powerQueue.get(0).equals("rForce1") | powerQueue.get(0).equals("rForce3"))) {
                            bSwitch2.setBackground(bOn);
                            rSwitch2.setBackground(rOff);
                            switch2 = -1;
                        }
                    }
                }
                break;

            case "btnBForce":
                if (bCubeAmt > 0 & !btnBForce.getText().equals("3") & !bPlaying) {
                    btnBForce.setText(String.valueOf(Integer.parseInt(btnBForce.getText()) + 1));
                    bCubeAmt--;
                    bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                    bPts += 5;
                }
                break;

            case "btnRForce":
                if (rCubeAmt > 0 & !btnRForce.getText().equals("3") & !rPlaying) {
                    btnRForce.setText(String.valueOf(Integer.parseInt(btnRForce.getText()) + 1));
                    rCubeAmt--;
                    rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                    rPts += 5;
                }
                break;

            case "btnBBoost":
                if (bCubeAmt > 0 & !btnBBoost.getText().equals("3") & !bPlaying) {
                    btnBBoost.setText(String.valueOf(Integer.parseInt(btnBBoost.getText()) + 1));
                    bCubeAmt--;
                    bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                    bPts += 5;
                }
                break;

            case "btnRBoost":
                if (rCubeAmt > 0 & !btnRBoost.getText().equals("3") & !rPlaying) {
                    btnRBoost.setText(String.valueOf(Integer.parseInt(btnRBoost.getText()) + 1));
                    rCubeAmt--;
                    rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                    rPts += 5;
                }
                break;

            case "btnBLevitate":
                if (bCubeAmt > 0 & !btnBLevitate.getText().equals("3") & !bPlaying) {
                    btnBLevitate.setText(String.valueOf(Integer.parseInt(btnBLevitate.getText()) + 1));
                    bCubeAmt--;
                    bCubes.setText("Cubes: " + String.valueOf(bCubeAmt));
                    bPts += 5;
                }
                break;

            case "btnRLevitate":
                if (rCubeAmt > 0 & !btnRLevitate.getText().equals("3") & !rPlaying) {
                    btnRLevitate.setText(String.valueOf(Integer.parseInt(btnRLevitate.getText()) + 1));
                    rCubeAmt--;
                    rCubes.setText("Cubes: " + String.valueOf(rCubeAmt));
                    rPts += 5;
                }
                break;

            case "cbxRLevitate":
                if (btnRLevitate.getText().equals("3")) {
                    cbxRLevitate.setEnabled(false);
                    btnRLevitate.setBackground(powerOn);
                } else cbxRLevitate.setSelected(false);
                break;

            case "cbxBLevitate":
                if (btnBLevitate.getText().equals("3")) {
                    cbxBLevitate.setEnabled(false);
                    btnBLevitate.setBackground(powerOn);
                } else cbxBLevitate.setSelected(false);
                break;

            case "cbxRForce":
                if (!btnRForce.getText().equals("0")) {
                    cbxRForce.setEnabled(false);
                    powerQueue.add("rForce" + btnRForce.getText());
                    btnRForce.setBackground(powerQueued);
                } else cbxRForce.setSelected(false);
                break;

            case "cbxBForce":
                if (!btnBForce.getText().equals("0")) {
                    cbxBForce.setEnabled(false);
                    powerQueue.add("bForce" + btnBForce.getText());
                    btnBForce.setBackground(powerQueued);
                } else cbxBForce.setSelected(false);
                break;

            case "cbxRBoost":
                if (!btnRBoost.getText().equals("0")) {
                    cbxRBoost.setEnabled(false);
                    powerQueue.add("rBoost" + btnRBoost.getText());
                    btnRBoost.setBackground(powerQueued);
                } else cbxRBoost.setSelected(false);
                break;

            case "cbxBBoost":
                if (!btnBBoost.getText().equals("0")) {
                    cbxBBoost.setEnabled(false);
                    powerQueue.add("bBoost" + btnBBoost.getText());
                    btnBBoost.setBackground(powerQueued);
                } else cbxBBoost.setSelected(false);
                break;

            case "playback":
                if (!rPath.getText().equals("Enter the red alliance's CSV file name here!")) {
                    rCommands = read(rPath.getText());
                    rPlaying = true;
                }
                if (!bPath.getText().equals("Enter the blue alliance's CSV file name here!")) {
                    bCommands = read(bPath.getText());
                    bPlaying = true;
                }
                if (!r2Path.getText().equals("Enter the red alliance's CSV file name here!")) {
                    r2Commands = read(r2Path.getText());
                    rPlaying = true;
                }
                if (!b2Path.getText().equals("Enter the blue alliance's CSV file name here!")) {
                    b2Commands = read(b2Path.getText());
                    bPlaying = true;
                }
                if (!r3Path.getText().equals("Enter the red alliance's CSV file name here!")) {
                    r3Commands = read(r3Path.getText());
                    rPlaying = true;
                }
                if (!b3Path.getText().equals("Enter the blue alliance's CSV file name here!")) {
                    b3Commands = read(b3Path.getText());
                    bPlaying = true;
                }
                break;
        }
    }

    // reads the given file path, breaks up CSV, and returns the values in a 2d array
    public String[][] read(String file) {
        String fileName = file, line;
        String[][] sepContents = new String[151][7];
        /*
        basically, x is the timestamp and y is command info. just enter the events of that timestamp and they are applied to the game
        example csv:
            .
            .
            .
            30, +boost (adds a block to boost)
            31, +boost, +force (adds blocks to force and boost)
            32, boost, mySwitch (activates the boost power up and adds a block to your switch)
            33, oppSwitch, +levitate, +levitate, +levitate (adds a block to the opponent side switch and 3 blocks to the levitate power up)
            34 (empty means no events occur, game continues as it should)
            35, levitate (activates the levitate power up)
            .
            .
            .
            each number represents the timestamp
            i didnt need them for the code, but it makes it easier for humans to write/read the CSV
        */
        int i = 0;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringTokenizer tokenizer;

            while((line = bufferedReader.readLine()) != null & i < 151) {
                tokenizer = new StringTokenizer(line, ",");
                int j = 0;
                while (tokenizer.hasMoreTokens() & j < 7) {
                    sepContents[i][j] = tokenizer.nextToken();
                    j++;
                }
                i++;
            }

            bufferedReader.close();
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }

        return sepContents;
    }

    public static void main (String[] args) {
        JFrame frame = new JFrame ("FIRST PowerUp Simulator 4");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new PowerUpSim());
        frame.pack();
        frame.setVisible (true);
    }
}
