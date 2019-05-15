package com.sol.model.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class TextConverter {
	
	//Convert vietnamese letters to unicode
	public String vietnameseToUnicode(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
}
