import java.io.*;
import java.util.HashMap;

public class assignment2 {
    // Hashmap for the different opcodes
    public static HashMap<Integer, String> opcodes = new HashMap<>();

    // Hashmap for the different conditions
    public static HashMap<Integer, String> conditions = new HashMap<>();

    // int to keep track of labelNumber
    public static int instructionCount = 1;

    public static void main(String[] args) {
        // Establishing the Hashmaps
        opcodes.put(0b10001011000, "ADD");
        opcodes.put(0b1001000100, "ADDI");
        opcodes.put(0b10001010000, "AND");
        opcodes.put(0b1001001000, "ANDI");
        opcodes.put(0b000101, "B");
        opcodes.put(0b01010100, "B.");
        opcodes.put(0b100101, "BL");
        opcodes.put(0b11010110000, "BR");
        opcodes.put(0b10110101, "CBNZ");
        opcodes.put(0b10110100, "CBZ");
        opcodes.put(0b11001010000, "EOR");
        opcodes.put(0b1101001000, "EORI");
        opcodes.put(0b11111000010, "LDUR");
        opcodes.put(0b11010011011, "LSL");
        opcodes.put(0b11010011010, "LSR");
        opcodes.put(0b10101010000, "ORR");
        opcodes.put(0b1011001000, "ORRI");
        opcodes.put(0b11111000000, "STUR");
        opcodes.put(0b11001011000, "SUB");
        opcodes.put(0b1101000100, "SUBI");
        opcodes.put(0b1111000100, "SUBIS");
        opcodes.put(0b11101011000, "SUBS");
        opcodes.put(0b10011011000, "MUL");
        opcodes.put(0b11111111101, "PRNT");
        opcodes.put(0b11111111100, "PRNL");
        opcodes.put(0b11111111110, "DUMP");
        opcodes.put(0b11111111111, "HALT");

        conditions.put(0x0, "EQ");
        conditions.put(0x1, "NE");
        conditions.put(0x2, "HS");
        conditions.put(0x3, "LO");
        conditions.put(0x4, "MI");
        conditions.put(0x5, "PL");
        conditions.put(0x6, "VS");
        conditions.put(0x7, "VC");
        conditions.put(0x8, "HI");
        conditions.put(0x9, "LS");
        conditions.put(0xa, "GE");
        conditions.put(0xb, "LT");
        conditions.put(0xc, "GT");
        conditions.put(0xd, "LE");

        // Read 4 bytes from a file and construct the binary instruction
        try {
            // args[0] takes in the first argument in the command (which will be the file name)
            File instructionsFile = new File(args[0]);
            DataInputStream readInstructions = new DataInputStream(new BufferedInputStream(new FileInputStream(instructionsFile)));

            // If bytes < 4, then the instruction set is done
            while(readInstructions.available() >= 4){
                /*
                    [& 0xFF] --> 0xFF = 0...011111111 which gives us the last 8 bits (the actual byte we want)
                    [<< 24] --> shifts the byte to the left 24 bits and pads 0's to the end
                 */
                byte firstByte = readInstructions.readByte();
                int firstByteInt = (firstByte & 0xFF) << 24;

                /*
                    [& 0xFF] --> 0xFF = 0...011111111 which gives us the last 8 bits (the actual byte we want)
                    [<< 16] --> shifts the byte to the left 16 bits and pads 0's to the end
                 */
                byte secondByte = readInstructions.readByte();
                int secondByteInt = (secondByte & 0xFF) << 16;

                /*
                    [& 0xFF] --> 0xFF = 0...011111111 which gives us the last 8 bits (the actual byte we want)
                    [<< 8] --> shifts the byte to the left 8 bits and pads 0's to the end
                 */
                byte thirdByte = readInstructions.readByte();
                int thirdByteInt = (thirdByte & 0xFF) << 8;

                /*
                    [& 0xFF] --> 0xFF = 0...011111111 which gives us the last 8 bits (the actual byte we want)
                 */
                byte fourthByte = readInstructions.readByte();
                int fourthByteInt = fourthByte & 0xFF;

                // [instruction] = [firstByte] + [secondByte] + [thirdByte] + [fourthByte]
                int instruction = firstByteInt + secondByteInt + thirdByteInt + fourthByteInt;

                disassemble(instruction);
            }
        }
        catch (IOException error){
            System.out.println(error);
        }
    }

    public static void disassemble(int instruction){
        // Initializing the string that will contain the instruction we disassemble
        String instructionString = "";

        // ---------------------------------- Get Opcodes ---------------------------------------------

        /*
            [>> 21] --> shifts the 11 bits to the right 21 bits and pads 1's to the beginning
            [& 0x7FF] --> 0x7FF = 0...011111111111 which gives us the last 11 bits (the actual bits we want)
        */
        int R_D_opcode = (instruction >> 21) & 0x7FF;

        /*
            [>> 22] --> shifts the 10 bits to the right 22 bits and pads 1's to the beginning
            [& 0x3FF] --> 0x3FF = 0...01111111111 which gives us the last 10 bits (the actual bits we want)
        */
        int I_opcode = (instruction >> 22) & 0x3FF;

        /*
            [>> 24] --> shifts the 8 bits to the right 24 bits and pads 1's to the beginning
            [& 0xFF] --> 0xFF = 0...011111111 which gives us the last 8 bits (the actual bits we want)
        */
        int CB_opcode = (instruction >> 24) & 0xFF;

        /*
            [>> 26] --> shifts the 6 bits to the right 26 bits and pads 1's to the beginning
            [& 0x3F] --> 0x3F = 0...0111111 which gives us the last 6 bits (the actual bits we want)
        */
        int B_opcode = (instruction >> 26) & 0x3F;

        // ---------------------------------- R and D type instructions  -------------------------------------

        if(opcodes.containsKey(R_D_opcode)){
            instructionString += opcodes.get(R_D_opcode);

            // If the instruction is ADD, AND, EOR, ORR, SUB, SUBS, MUL
            if((R_D_opcode == 0b10001011000) || (R_D_opcode == 0b10001010000) || (R_D_opcode == 0b11001010000) || (R_D_opcode == 0b10101010000) || (R_D_opcode == 0b11001011000) || (R_D_opcode == 0b11101011000) || (R_D_opcode == 0b10011011000)){
                /*
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rd = instruction & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28){
                    RdString = "SP";
                }
                else if(Rd == 29){
                    RdString = "FP";
                }
                else if(Rd == 30){
                    RdString = "LR";
                }
                else if(Rd == 31){
                    RdString = "XZR";
                }

                /*
                    [>> 5] --> shifts the 27 bits to the right 5 bits and pads 1's to the beginning
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                /*
                    [>> 16] --> shifts the 16 bits to the right 16 bits and pads 1's to the beginning
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rm = instruction >> 16 & 0x1F;
                String RmString = "X" + Rm;
                if(Rm == 28){
                    RmString = "SP";
                }
                else if(Rm == 29){
                    RmString = "FP";
                }
                else if(Rm == 30){
                    RmString = "LR";
                }
                else if(Rm == 31){
                    RmString = "XZR";
                }

                instructionString += " " + RdString + ", " + RnString + ", " + RmString;
            }

            // If the instruction is BR
            else if(R_D_opcode == 0b11010110000){
                /*
                    [>> 5] --> shifts the 27 bits to the right 5 bits and pads 1's to the beginning
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                instructionString += " " + RnString;
            }

            // If the instruction is LSL, LSR
            else if((R_D_opcode == 0b11010011011) || (R_D_opcode == 0b11010011010)) {
                /*
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rd = instruction & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28){
                    RdString = "SP";
                }
                else if(Rd == 29){
                    RdString = "FP";
                }
                else if(Rd == 30){
                    RdString = "LR";
                }
                else if(Rd == 31){
                    RdString = "XZR";
                }

                /*
                    [>> 5] --> shifts the 27 bits to the right 5 bits and pads 1's to the beginning
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                /*
                    [>> 10] --> shifts the 22 bits to the right 10 bits and pads 1's to the beginning
                    [& 0x3F] --> 0x3F = 0...0111111 which gives us the last 6 bits (the actual bits we want)
                */
                int shamt = instruction >> 10 & 0x3F;
                // Number is negative (but disassembler won't treat it like one)
                if(shamt >= 32){
                    // Actually make it negative
                    shamt -= 64;
                }

                instructionString += " " + RdString + ", " + RnString + ", #" + shamt;
            }

            // If the instruction is PRNT
            else if(R_D_opcode == 0b11111111101){
                /*
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rd = instruction & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28){
                    RdString = "SP";
                }
                else if(Rd == 29){
                    RdString = "FP";
                }
                else if(Rd == 30){
                    RdString = "LR";
                }
                else if(Rd == 31){
                    RdString = "XZR";
                }

                instructionString += " " + RdString;
            }

            // If the instruction is LDUR, STUR
            else if((R_D_opcode == 0b11111000010) || (R_D_opcode == 0b11111000000)){
                /*
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rt = instruction & 0x1F;
                String RtString = "X" + Rt;
                if(Rt == 28){
                    RtString = "SP";
                }
                else if(Rt == 29){
                    RtString = "FP";
                }
                else if(Rt == 30){
                    RtString = "LR";
                }
                else if(Rt == 31){
                    RtString = "XZR";
                }

                /*
                    [>> 5] --> shifts the 27 bits to the right 5 bits and pads 1's to the beginning
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                /*
                    [>> 12] --> shifts the 20 bits to the right 12 bits and pads 1's to the beginning
                    [& 0x1FF] --> 0x1FF = 0...0111111111 which gives us the last 9 bits (the actual bits we want)
                */
                int DTAddr = instruction >> 12 & 0x1FF;
                // Number is negative (but disassembler won't treat it like one)
                if(DTAddr >= 256){
                    // Actually make it negative
                    DTAddr -= 512;
                }

                instructionString += " " + RtString + ", [" + RnString + ", #" + DTAddr + "]";
            }

            else{
                // Nothing, just the instruction name
            }
        }

        // ------------------------------------- I type instructions  ----------------------------------------

        else if(opcodes.containsKey(I_opcode)){
            instructionString += opcodes.get(I_opcode);

            /*
                [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
            */
            int Rd = instruction & 0x1F;
            String RdString = "X" + Rd;
            if(Rd == 28){
                RdString = "SP";
            }
            else if(Rd == 29){
                RdString = "FP";
            }
            else if(Rd == 30){
                RdString = "LR";
            }
            else if(Rd == 31) {
                RdString = "XZR";
            }

            /*
                [>> 5] --> shifts the 27 bits to the right 5 bits and pads 1's to the beginning
                [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
            */
            int Rn = instruction >> 5 & 0x1F;
            String RnString = "X" + Rn;
            if(Rn == 28){
                RnString = "SP";
            }
            else if(Rn == 29){
                RnString = "FP";
            }
            else if(Rn == 30){
                RnString = "LR";
            }
            else if(Rn == 31){
                RnString = "XZR";
            }

            /*
                [>> 10] --> shifts the 22 bits to the right 10 bits and pads 1's to the beginning
                [& 0xFFF] --> 0x1F = 0...0111111111111 which gives us the last 12 bits (the actual bits we want)
            */
            int ALUImm = instruction >> 10 & 0xFFF;
            // Number is negative (but disassembler won't treat it like one)
            if(ALUImm >= 2048){
                // Actually make it negative
                ALUImm -= 4096;
            }

            instructionString += " " + RdString + ", " + RnString + ", #" + ALUImm;
        }

        // ------------------------------------- CB type instructions  ----------------------------------------

        else if(opcodes.containsKey(CB_opcode)){
            instructionString += opcodes.get(CB_opcode);

            // If the instruction is B.
            if(CB_opcode == 0b01010100){
                /*
                    [& 0x1F] --> 0x1F = 0...011111 which gives us the last 5 bits (the actual bits we want)
                */
                int cond = instruction & 0x1F;
                String condString = conditions.get(cond);
                instructionString += condString;
            }

            /*
                [& 0x7FFFF] --> 0x7FFFF = 0...01111111111111111111 which gives us the last 19 bits (the actual bits we want)
            */
            int BranchAddr = instruction >> 5 & 0x7FFFF;
            // Number is negative (but disassembler won't treat it like one)
            if(BranchAddr >= 262144){
                // Actually make it negative
                BranchAddr -= 524288;
            }

            instructionString += " L" + (instructionCount + BranchAddr);
        }

        // ------------------------------------- B type instructions  ----------------------------------------

        else if(opcodes.containsKey(B_opcode)){
            /*
                [& 0x3FFFFFF] --> 0x3FFFFFF = 0...011111111111111111111111111 which gives us the last 26 bits (the actual bits we want)
            */
            int BranchAddr = instruction & 0x3FFFFFF;
            // Number is negative (but disassembler won't treat it like one)
            if(BranchAddr >= 33554432){
                // Actually make it negative
                BranchAddr -= 67108864;
            }

            instructionString += opcodes.get(B_opcode) + " L" + (instructionCount + BranchAddr);
        }

        // ------------------------------------ Instruction not found  --------------------------------------

        else{
            System.out.println("Opcode not found --> Error with program");
        }

        // -------------------------------- Print the Instruction ------------------------------------

        System.out.println("L" + instructionCount + ": " + instructionString);
        instructionCount++;
    }
}