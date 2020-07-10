package com.using.customer.web.client.controller;

import com.using.common.core.bean.ErrorEnum;
import com.using.common.core.bean.JsonResult;
import com.using.common.core.exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/importAmount")
    public JsonResult importAmount(MultipartFile multipartFile) throws BusinessException {
        JsonResult<Object> jsonResult = new JsonResult<>();
        if(!multipartFile.getOriginalFilename().endsWith(".zip")){
            throw new BusinessException(ErrorEnum.ERROR_PARAM,"文件格式有误，请重新选择文件");
        }
        try {
            //将zip压缩包存入系统
            File file = null;
            byte[] bytes = multipartFile.getBytes();
            file = new File("存压缩包的路径");
            if(!file.exists())
                file.mkdirs();
            Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
            Files.write(path,bytes);

            //解压压缩包
            CompressFileUits.unZipFiles(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename(),"解压后的文件夹目录");
            File[] files = new File("解压后的文件夹目录").listFiles();
            for (File excelFile : files) {
                //判断excel文件格式
                Workbook workbook =null;
                if(excelFile.getName().endsWith(".xlsx") || excelFile.getName().endsWith(".xls")){
                    FileInputStream inputStream = new FileInputStream(excelFile.getAbsoluteFile());
                    if(excelFile.getName().endsWith(".xlsx")){
                        workbook = new XSSFWorkbook(inputStream);
                    }else if(excelFile.getName().endsWith(".xls")){
                        workbook = new HSSFWorkbook(inputStream);
                    }
                    //获取的是第0个sheet页数据
                    Sheet sheetAt = workbook.getSheetAt(0);
                    for (int i = 0; i <= sheetAt.getLastRowNum(); i++) {
                        Row row = sheetAt.getRow(i);
                        //第a列数据
                        String a = row.getCell(0).getStringCellValue();
                        //第b列数据
                        String b = row.getCell(1).getStringCellValue();
                        //.......
                        //调接口存入数据库
                    }
                }else{
                    throw new BusinessException(ErrorEnum.ERROR_PARAM,"压缩包中文件格式有误");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
