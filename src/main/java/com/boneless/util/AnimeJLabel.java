package com.boneless.util;

import javax.swing.*;
import java.awt.Color;

    public class AnimeJLabel extends JLabel
    {
        private static final long serialVersionUID = 1L;
        private Color startColour;

        private Thread t;
        private boolean firstTime;
        private long waitTime;

        private boolean stopTxtAniamBool;

        private int StartR,StartG,StartB;
        private int MidR,MidG,MidB;
        private int EndR,EndG,EndB;


        public AnimeJLabel(Color startColour,Color endColour)
        {
            this(startColour,endColour,5);
        }

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
            waitTime = sec * 1000;
        }

        public void setTxt(String txt)
        {
            MidR = StartR;
            MidG = StartG;
            MidB = StartB;

            stopT();

            this.setForeground(startColour);
            this.setText(" "+txt);
            if( ! txt.equals(""))//if theres No text then there no need to blend
            {
                try
                {
                    t = new Thread(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                Thread.sleep(waitTime);

                                while(true == start2end_colour()){
                                    Thread.sleep(10);
                                }

                            }
                            catch(Exception ex){
                                //System.out.println("Error:Sleep");
                                //System.out.println(ex);
                            }
                        }
                    });
                    t.start();
                }
                catch(Exception ex){
                    System.out.println("Error:Nova_JLabel");
                }
            }
        }

        public void stopTxtAniam(){
            stopTxtAniamBool = true;
        }


        private void stopT()
        {
            if(false == firstTime){
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
                t = new Thread(new Runnable()
                {
                    public void run(){
                        try
                        {
                            while(true == start2end_colour()){
                                Thread.sleep(2);
                            }
                            setPlaneTxt(txt);

                            while(true == end2start_colour()){
                                Thread.sleep(2);
                            }
                        }
                        catch(Exception ex){
                        }
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
                t = new Thread(new Runnable()
                {
                    public void run()
                    {
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
                        catch(Exception ex){
                        }
                    }
                });
                t.start();
            }
            catch(Exception ex){
                System.out.println("Error:CountDown");
            }
        }

        public void setTxtAniam(final String[] txtArray, final long timeInMill) {
            stopTxtAniamBool = false;
            stopT();

            try {
                t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            for (int i = 0; i < txtArray.length; i++) {
                                if (stopTxtAniamBool) // Check if we need to stop the animation
                                    break;

                                setPlaneTxt(txtArray[i]);
                                Thread.sleep(timeInMill);
                            }
                            // Additional animation or cleanup after the loop ends
                            start2end_colour();
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt(); // Restore interrupted status
                        } catch (Exception ex) {
                            ex.printStackTrace(); // Handle other exceptions appropriately
                        }
                    }
                });
                t.start();
            } catch (Exception ex) {
                System.out.println("Error: setTxtAniam");
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

            if(1 == intR && 1 == intB && 1 == intG)
            {  return false;  }
            else
            {  return true;  }
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

            if(1 == intR && 1 == intB && 1 == intG)
            { return false;  }
            else
            { return true; }
        }
    }
