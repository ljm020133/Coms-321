import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LEGv8Disassembler {    

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java LEGv8Disassembler <binary_file>");
            System.exit(1);
        }

        String binaryFileName = args[0];

        try {
            byte[] binaryData = readBinaryFile(binaryFileName);

            for (int i = 0; i < binaryData.length; i += 4) {
                int currInstr = ByteBuffer.wrap(binaryData, i, 4)
                        .order(ByteOrder.BIG_ENDIAN)
                        .getInt();

                String disassembledInstruction = decodeInstruction(currInstr);
                System.out.println(disassembledInstruction);
            }

        } catch (IOException e) {
            System.err.println("Error reading binary file: " + e.getMessage());
        }
    }

    private static byte[] readBinaryFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllBytes(path);
    }

    return switch (opcode) {
            case 0b10001011000 -> decodeADD(currInstr);
            case 0b10010001000 -> decodeADDI(currInstr);
            case 0b1001000100 -> decodeSUB(currInstr);
            case 0b11010001000 -> decodeSUBI(currInstr);
            case 0b11110001000 -> decodeSUBIS(currInstr);
            case 0b11110010000 -> decodeSUBS(currInstr);
            case 0b00010100000 -> decodeB(currInstr);
            case 0b10010100000 -> decodeBL(currInstr);
            case 0b11010110000 -> decodeBR(currInstr);
            case 0b01010100000 -> decodeBCond(currInstr);
            case 0b10101000000 -> decodeCB(currInstr);
            case 0b10110101000 -> decodeCBNZ(currInstr);
            case 0b10110100000 -> decodeCBZ(currInstr);
            case 0b11010011010 -> decodeLSR(currInstr);
            case 0b11010011011 -> decodeLSL(currInstr);
            case 0b10101010000 -> decodeORR(currInstr);
            case 0b10110010000 -> decodeORRI(currInstr);
            case 0b11001010000 -> decodeEOR(currInstr);
            case 0b11010010000 -> decodeEORI(currInstr);
            case 0b10001010000 -> decodeAND(currInstr);
            case 0b10010010000 -> decodeANDI(currInstr);
            case 0b11111000010 -> decodeLDUR(currInstr);
            case 0b11111000000 -> decodeSTUR(currInstr);
            case 0b10011011000 -> decodeMUL(currInstr);
            case 0b11111111101 -> decodePRNT(currInstr);
            case 0b11111111100 -> decodePRNL(currInstr);
            case 0b11111111110 -> decodeDUMP(currInstr);
            case 0b11111111111 -> decodeHALT(currInstr);
            default -> String.format("Unknown instruction: 0x%08X", currInstr);
        };

    private static String decodeADD(int instruction) {

        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 16) & 0b11111;

        return String.format("ADD X%d, X%d, X%d", Rd, Rn, Rm);
    }

    private static String decodeADDI(int instruction) {

        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int immediate = (instruction >>> 10) & 0b1111111111;

        return String.format("ADDI X%d, X%d, #%d", Rd, Rn, immediate);
    }

    private static String decodeSUB(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 10) & 0b11111;

        return String.format("SUB X%d, X%d, X%d", Rd, Rn, Rm);
    }

    private static String decodeAND(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 16) & 0b11111;

        return String.format("AND X%d, X%d, X%d", Rd, Rn, Rm);
    }

    private static String decodeANDI(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int immediate = (instruction >>> 10) & 0b1111111111;

        return String.format("ANDI X%d, X%d, #%d", Rd, Rn, immediate);
    }

    private static String decodeB(int instruction) {
        int offset = (instruction & 0b1111111111111111111111) << 2;

        return String.format("B #%d", offset);
    }

    private static String decodeCB(int instruction) {
        int condCode = (instruction >>> 20) & 0b1111;

        return String.format("B.cond %s", getConditionCodeName(condCode));
    }

    private static String decodeBCond(int instruction) {
        int condCode = instruction & 0b1111;
        int offset = (instruction & 0b1111111111111111) << 2;

        return String.format("B.%s #%d", getConditionCodeName(condCode), offset);
    }

    private static String decodeBL(int instruction) {
        int offset = instruction & 0x3FFFFFF;

        return String.format("BL #%d", offset);
    }

    private static String decodeBR(int instruction) {
        int Rn = (instruction >>> 5) & 0b11111;

        return String.format("BR X%d", Rn);
    }

    private static String decodeCBNZ(int instruction) {
        int Rt = instruction & 0b11111;
        int offset = (instruction >>> 5) & 0x7FFFF;

        return String.format("CBNZ X%d, #%d", Rt, offset);
    }

    private static String decodeCBZ(int instruction) {
        int Rt = instruction & 0b11111;
        int offset = (instruction >>> 5) & 0x7FFFF;

        return String.format("CBZ X%d, #%d", Rt, offset);
    }

    private static String decodeEOR(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 10) & 0b11111;

        return String.format("EOR X%d, X%d, X%d", Rd, Rn, Rm);
    }

    private static String decodeEORI(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int immediate = (instruction >>> 10) & 0b1111111111;

        return String.format("EORI X%d, X%d, #%d", Rd, Rn, immediate);
    }

    private static String decodeLDUR(int instruction) {
        int Rt = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int offset = (instruction >>> 10) & 0b1111111111;

        return String.format("LDUR X%d, [X%d, #%d]", Rt, Rn, offset);
    }

    private static String decodeLSL(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int shiftAmount = (instruction >>> 16) & 0b111111;

        return String.format("LSL X%d, X%d, #%d", Rd, Rn, shiftAmount);
    }

    private static String decodeLSR(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int shiftAmount = (instruction >>> 16) & 0b111111;

        return String.format("LSR X%d, X%d, #%d", Rd, Rn, shiftAmount);
    }

    private static String decodeORR(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 10) & 0b11111;
        int shamt = (instruction >>> 16) & 0b11111;

        return String.format("ORR X%d, X%d, X%d, #%d", Rd, Rn, Rm, shamt);
    }

    private static String decodeORRI(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int immediate = (instruction >>> 10) & 0b1111111111;

        return String.format("ORRI X%d, X%d, #%d", Rd, Rn, immediate);
    }

    private static String decodeSTUR(int instruction) {
        int Rt = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int offset = (instruction >>> 10) & 0b1111111111;

        return String.format("STUR X%d, [X%d, #%d]", Rt, Rn, offset);
    }

    private static String decodeSUBI(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int immediate = (instruction >>> 10) & 0b1111111111;

        return String.format("SUBI X%d, X%d, #%d", Rd, Rn, immediate);
    }

    private static String decodeSUBIS(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int immediate = (instruction >>> 10) & 0b1111111111;

        return String.format("SUBIS X%d, X%d, #%d", Rd, Rn, immediate);
    }

    private static String decodeSUBS(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 10) & 0b11111;
        int shamt = (instruction >>> 16) & 0b11111;

        return String.format("SUBS X%d, X%d, X%d, #%d", Rd, Rn, Rm, shamt);
    }

    private static String decodeMUL(int instruction) {
        int Rd = (instruction >>> 0) & 0b11111;
        int Rn = (instruction >>> 5) & 0b11111;
        int Rm = (instruction >>> 10) & 0b11111;

        return String.format("MUL X%d, X%d, X%d", Rd, Rn, Rm);
    }

    private static String decodePRNT(int instruction) {
        int Rd = (instruction >>> 5) & 0b11111;

        return String.format("PRNT X%d", Rd);
    }

    private static String decodePRNL(int instruction) {
        return "PRNL";
    }

    private static String decodeDUMP(int instruction) {
        return "DUMP";
    }

    private static String decodeHALT(int instruction) {
        return "HALT";
    }

    private static String getCondition(int code) {
        switch (code) {
            case 0x0: return "EQ";
            case 0x1: return "NE";
            case 0x2: return "HS";
            case 0x3: return "LO";
            case 0x4: return "MI";
            case 0x5: return "PL";
            case 0x6: return "VS";
            case 0x7: return "VC";
            case 0x8: return "HI";
            case 0x9: return "LS";
            case 0xA: return "GE";
            case 0xB: return "LT";
            case 0xC: return "GT";
            case 0xD: return "LE";
            default: return "UNKNOWN";
        }
    }
}
