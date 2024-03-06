package com.boneless;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class RandomScalingPanelExample {
    public static void main(String[] args) {
        int[] num = {1,2,3,4,5,6};
        System.out.println(firstLast6(num));
    }
    public static boolean firstLast6(int[] nums) {
        int length = nums.length - 1;
        if(nums[0] == 6 || nums[length] == 6){
            return true;
        }
        return false;
    }

}
