package com.scalefocus;

import com.scalefocus.utils.Helper;
import com.scalefocus.view.IndexView;
import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        IndexView.login(false);
        Helper.sayBye();
    }
}
