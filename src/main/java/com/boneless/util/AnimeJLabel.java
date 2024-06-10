package com.boneless.util;

import javax.swing.*;
import java.awt.Color;
import java.io.Serial;

@SuppressWarnings("BusyWait")
public class AnimeJLabel extends JLabel
    {
        @Serial
        private static final long serialVersionUID = 1L;
        private final Color startColour;
        private Thread t;
        private boolean firstTime;
        private final long waitTime;
        private boolean stopTxtAnimBool;
        private final int StartR;
        private final int StartG;
        private final int StartB;
        private int MidR,MidG,MidB;
        private final int EndR;
        private final int EndG;
        private final int EndB;

        public AnimeJLabel(Color startColour,Color endColour,int sec)
        {
            this.setForeground(startColour);
            this.startColour = startColour;

            StartR = startColour.getRed();
            StartG = startColour.getGreen();
            StartB = startColour.getBlue();

            EndR = endColour.getRed();
            EndG = endColour.getGreen();
            EndB = endColour.getBlue();

            firstTime = true;
            waitTime = sec * 1000L;
        }

        public void setTxt(String txt)
        {
            MidR = StartR;
            MidG = StartG;
            MidB = StartB;

            stopT();

            this.setForeground(startColour);
            this.setText(" "+txt);
            if(!txt.isEmpty())//if there's No text then there no need to blend
            {
                try
                {
                    t = new Thread(() -> {
                        try
                        {
                            Thread.sleep(waitTime);

                            while(start2end_colour()){
                                Thread.sleep(10);
                            }

                        }
                        catch(Exception ignored){
                        }
                    });
                    t.start();
                }
                catch(Exception ex){
                    System.out.println("Error:Nova_JLabel");
                }
            }
        }

        private void stopT()
        {
            if(!firstTime){
                t.interrupt();
            }
            else{
                firstTime = false;
            }
        }

        private void setPlaneTxt(String txt)
        {
            this.setText(" "+txt);
        }

        public void setInterFadeTxt(final String txt)
        {
            stopT();

            try
            {
                t = new Thread(() -> {
                    try
                    {
                        while(start2end_colour()){
                            Thread.sleep(2);
                        }
                        setPlaneTxt(txt);

                        while(end2start_colour()){
                            Thread.sleep(2);
                        }
                    }
                    catch(Exception ignored){
                    }
                });
                t.start();
            }
            catch(Exception ex)
            {
                System.out.println("Error:CountDown");
            }
        }

        public void CountDown(int Sec){
            CountDown(Sec,"","Finished");
        }

        public void CountDown(final int Sec,final String displayMessage,final String endMessage)
        {
            stopT();

            try
            {
                t = new Thread(() -> {
                    try
                    {
                        for(int i = Sec-1; 0<i; i--)
                        {
                            for(int ii = 100; 0<ii; ii-=2)//go up in 2
                            {
                                setPlaneTxt(displayMessage +" "+i+"."+ii);
                                Thread.sleep(20);
                            }
                        }
                        setPlaneTxt(endMessage);

                    }
                    catch(Exception ignored){
                    }
                });
                t.start();
            }
            catch(Exception ex){
                System.out.println("Error:CountDown");
            }
        }

        public void setTxtAnim(final String[] txtArray, final long timeInMill) {
            stopTxtAnimBool = false;
            stopT();

            try {
                t = new Thread(() -> {
                    try {
                        for (String s : txtArray) {
                            if (stopTxtAnimBool) // Check if we need to stop the animation
                                break;

                            setPlaneTxt(s);
                            Thread.sleep(timeInMill);
                        }
                        // Additional animation or cleanup after the loop ends
//                            start2end_colour();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt(); // Restore interrupted status
                    } catch (Exception ex) {
                        ex.printStackTrace(); // Handle other exceptions appropriately
                    }
                });
                t.start();
            } catch (Exception ex) {
                System.out.println("Error: setTxtAnim");
            }
        }


        private boolean start2end_colour()
        {

            int intR = 0;
            int intG = 0;
            int intB = 0;

            this.setForeground(new Color(MidR,MidG,MidB));

            if(MidR == EndR)
            {  intR = 1;  }
            else if(EndR>MidR)
            {  MidR++;  }
            else// if(EndR<MidR)
            {  MidR--;  }
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            if(MidG == EndG)
            {  intG = 1;  }
            else if(EndG>MidG)
            {  MidG++;  }
            else// if(EndG<MidG)
            {  MidG--;  }
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            if(MidB == EndB)
            {  intB = 1;  }
            else if(EndB>MidB)
            {  MidB++;  }
            else// if(EndB<MidB)
            {  MidB--;  }

            return 1 != intR || 1 != intB || 1 != intG;
        }

        private boolean end2start_colour()
        {
            int intR = 0;
            int intG = 0;
            int intB = 0;

            this.setForeground(new Color(MidR,MidG,MidB));

            if(MidR == StartR)
            { intR = 1;  }
            else if(StartR>MidR)
            { MidR++;  }
            else// if(EndR>MidR)
            { MidR--;  }
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            if(MidG == StartG)
            { intG = 1;  }
            else if(StartG>MidG)
            { MidG++;  }
            else// if(EndG>MidG)
            { MidG--;  }
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            if(MidB == StartB)
            { intB = 1;  }
            else if(StartB>MidB)
            { MidB++;  }
            else// if(EndB>MidB)
            { MidB--;  }

            return 1 != intR || 1 != intB || 1 != intG;
        }
    }