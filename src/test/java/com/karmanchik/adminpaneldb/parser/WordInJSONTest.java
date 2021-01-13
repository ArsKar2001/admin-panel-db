package com.karmanchik.adminpaneldb.parser;

import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Log4j
class WordInJSONTest {
    @Test
    public void testParsingWordFile() {
        File file = new File("src\\main\\resources\\files\\docx\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx");
        WordInJSON wordInJSON = new WordInJSON();
        String text = wordInJSON.wordFileAsText(file);
        System.out.println(text);
    }
}