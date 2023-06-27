package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.oj.server.entity.RankInfo;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.ExcelExporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author march
 * @since 2023/6/10 上午8:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "排名信息")
public class RankInfoVO implements ExcelExporter {
    /**
     * 用户
     */
    @Schema(description = "用户")
    private UserProfileVO user;

    /**
     * 过题数
     */
    @Schema(description = "过题数")
    private Integer count;

    /**
     * 罚时
     */
    @Schema(description = "总罚时")
    private String penalty;

    /**
     * 题目状态
     * problemid - state
     */
    @Schema(description = "题目状态")
    private List<ProblemStateVO> problemStates;

    public static RankInfoVO of(RankInfo rankInfo) {
        return BeanCopyUtils.copyObject(rankInfo, RankInfoVO.class);
    }

    @Override
    public void setRow(SXSSFRow row, SXSSFWorkbook workbook) {
        row.createCell(0).setCellValue(user.getNickname());
        row.createCell(1).setCellValue(user.getName());
        row.createCell(2).setCellValue(user.getNumber());
        row.createCell(3).setCellValue(count);
        row.createCell(4).setCellValue(penalty);
        for (int i = 0; i < problemStates.size(); i ++ ) {
            ProblemStateVO state = problemStates.get(i);

            SXSSFCell cell = row.createCell(i + 5);
            CellStyle cellStyle = workbook.createCellStyle();
            if (state.getFirstBlood()) { // 一血
                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            } else if (state.getIsAccept()) { // AC
                cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            }
            cell.setCellStyle(cellStyle);
            cell.setCellValue(state.getNumber());
        }
    }

    public static List<String> getHeader(List<String> titles) {
        List<String> list = new ArrayList<>(5 + titles.size());
        list.add("昵称");
        list.add("名字");
        list.add("学号");
        list.add("过题数");
        list.add("罚时");
        list.addAll(titles);
        return list;
    }
}
