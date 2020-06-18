package biokey;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.*;

public class GUI extends JFrame{
    private final JTextField input = new JTextField("");  
    private final JLabel label = new JLabel("Input pass and press Enter");    
    private final JLabel successLabel = new JLabel("Success");    
    private JLabel numOfKeysLabel = new JLabel("0");    
    private final JRadioButton learn = new JRadioButton("Learn"); 
    private final JRadioButton check = new JRadioButton("Check"); 
    private final ButtonGroup modeGroup = new ButtonGroup();  
    private int numOfKeys = 0;  
    private boolean isWatchStarted = false; 
    private long startedTime = 0;
    private ArrayList<Integer> keyList = new ArrayList<>(); 
    private ArrayList<Long> timeModel = new ArrayList<>();    
    private ArrayList<Integer> keysModel = new ArrayList<>();    
    private ArrayList<Long> newTimeModel = new ArrayList<>();    
    private ArrayList<Integer> newKeysModel = new ArrayList<>();
    private final java.util.Timer successTimer = new java.util.Timer("Timer");    
    private String password = new String();
    
    public void statusMessage(boolean isSuccess){  
        if (!successLabel.isVisible()){
            if(isSuccess){
                successLabel.setText("Success");
                successLabel.setForeground(Color.GREEN);
            } else{
                successLabel.setText("Wrong");
                successLabel.setForeground(Color.RED);                
            }
            successLabel.setVisible(true);
            input.setText("");
            successTimer.schedule(new TimerTask(){
                @Override
                public void run() {
                    successLabel.setVisible(false);
                }
            }, 2000L);
        }
    }
    
    
    
    public GUI(){
        super("BioKey");
        
        this.setBounds(100,100,350,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        modeGroup.add(learn);
        modeGroup.add(check);
        check.setSelected(true);
        
        successLabel.setVisible(false);
        
        for(int i = 33; i < 127; i++){
            keyList.add(i);
        }
        
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,2,2,2));
        container.add(label);
        container.add(input);
        container.add(learn);
        container.add(check);
        container.add(numOfKeysLabel);
        container.add(successLabel);
        
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                //System.out.println(ke);
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (keyList.contains(ke.getKeyCode())){
                    numOfKeys++;
                    numOfKeysLabel.setText(""+numOfKeys);
                    if(!isWatchStarted){
                        isWatchStarted = true;
                        startedTime = System.currentTimeMillis();
                        newKeysModel.clear();
                        newTimeModel.clear();
                    }
                    newTimeModel.add((System.currentTimeMillis() - startedTime)/400);
                    newKeysModel.add(numOfKeys);                 
                }
                if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                    isWatchStarted = false;
                    if (learn.isSelected()){
                        password = input.getText();
                        timeModel = (ArrayList<Long>) newTimeModel.clone();
                        keysModel = (ArrayList<Integer>) newKeysModel.clone();
                        statusMessage(true);
                    } else{
                        System.out.println(input.getText()+" | "+password + " | " + input.getText().equals(password));
                        System.out.println(newKeysModel+" | "+keysModel+ " | " + newKeysModel.equals(keysModel));
                        System.out.println(newTimeModel+" | "+timeModel+ " | " + newTimeModel.equals(timeModel));
                        if(input.getText().equals(password) && newKeysModel.equals(keysModel) && newTimeModel.equals(timeModel)){
                            statusMessage(true);
                            JOptionPane.showMessageDialog(null, "Correct");
                        } else{
                            statusMessage(false);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (keyList.contains(ke.getKeyCode())){
                    numOfKeys--;
                    numOfKeysLabel.setText(""+numOfKeys);
                }
            }
        });
    }
}
