package com.example.springreact.playground;

public class Telephone {
    public final static int FORMATTED_LENGTH = 13;
    public final static int UNFORMATTED_LENGTH = 10;



    public static boolean isFormatted(String numberValue){
        boolean isValid;

        if (numberValue.length() == FORMATTED_LENGTH && numberValue.charAt(0) ==
        '(' && numberValue.charAt(4) == '(' && numberValue.charAt(8)== '-'){
            isValid = true;
        }
        else{
            isValid = false;
        }
        return isValid;
    }

    public static String Unformatted(String value){
        StringBuilder builder=new StringBuilder(value);
        if (isFormatted(value)){

            // delete the left parenthesis
            builder.deleteCharAt(0);

            // delete the right parenthesis
            builder.deleteCharAt(3);

            // delete the hyphen
            builder.deleteCharAt(6);
        }

        // return the unformatted string...
        return builder.toString();
    }

    public static String format(String str){
        StringBuilder builder=new StringBuilder(str);

        if (str.length() == UNFORMATTED_LENGTH){

            // reinsert the first paren at position 0, remember counts begin at 0
            builder.insert(0, "(");

            // reinsert the second paren at position 4
            builder.insert(4, ")");

            // lastly, insert the hyphen at position 8.
            builder.insert(8, "-");
        }
        return builder.toString();
    }
}
