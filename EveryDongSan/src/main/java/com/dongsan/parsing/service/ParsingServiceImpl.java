package com.dongsan.parsing.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.dongsan.home.entity.HomeEntity;
import com.dongsan.home.model.Home;
import com.dongsan.parsing.mapper.ParsingMapper;

@Service
public class ParsingServiceImpl implements ParsingService{

	@Autowired
	private ParsingMapper parsingMapper;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void parseAndSaveRealEstateData(String fileName) throws Exception {
		List<HomeEntity> homeList = new ArrayList<>();
		
		try (InputStream is = resourceLoader.getResource("classpath:" + fileName).getInputStream();
	             Workbook workbook = new XSSFWorkbook(is)) {

	            Sheet sheet = workbook.getSheetAt(1);

	            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 첫 번째 행은 헤더로 건너뜁니다.
	                Row row = sheet.getRow(i);
	                HomeEntity data = new HomeEntity();
	                data.setName(getStringValue(row.getCell(0)));
	                data.setCategory(getStringValue(row.getCell(1)));
	                data.setRentalType(getStringValue(row.getCell(2)));
	                data.setPrice(getIntValue(row.getCell(3)) + "/" + getIntValue(row.getCell(4)));
	                data.setArea(getBigDecimalValue(row.getCell(5)));
	                data.setLatitude(getBigDecimalValue(row.getCell(6)));
	                data.setLongitude(getBigDecimalValue(row.getCell(7)));
	                data.setSchoolId(getIntValue(row.getCell(8)));
	                data.setMemberId("hanmin");
	                
	                homeList.add(data);
	            }
	            parsingMapper.insertHomeData(homeList);
	        }
		
	}
	
	private int getIntValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    return Integer.parseInt(cell.getStringCellValue());
                default:
                    return 0; // 또는 적절한 기본값 설정
            }
        }
        return 0; // 셀이 null인 경우 기본값
    }

    private double getDoubleValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    return Double.parseDouble(cell.getStringCellValue());
                default:
                    return 0.0; // 또는 적절한 기본값 설정
            }
        }
        return 0.0; // 셀이 null인 경우 기본값
    }

    private String getStringValue(Cell cell) {
        if (cell != null) {
            return cell.getStringCellValue();
        }
        return ""; // 셀이 null인 경우 기본값
    }
    
    private BigDecimal getBigDecimalValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    try {
                        return new BigDecimal(cell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        return BigDecimal.ZERO; // 숫자로 변환할 수 없는 경우 기본값 반환
                    }
                default:
                    return BigDecimal.ZERO; // 기본값
            }
        }
        return BigDecimal.ZERO; // 셀이 null인 경우 기본값
    }
	
	
}
