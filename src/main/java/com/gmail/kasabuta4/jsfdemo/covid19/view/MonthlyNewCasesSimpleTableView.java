package com.gmail.kasabuta4.jsfdemo.covid19.view;

import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidths.AUTO_SIZE;
import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidths.byCharacters;
import static java.util.Collections.unmodifiableMap;

import com.gmail.kasabuta4.jsfdemo.common.facade.SimpleSearchFacade;
import com.gmail.kasabuta4.jsfdemo.common.view.TableView;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.CellStyles;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.Colors;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.Fonts;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.WorkbookModel;
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlConverters;
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlSimpleTable;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesSimpleTableFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Named
@RequestScoped
public class MonthlyNewCasesSimpleTableView
    extends TableView<
        SearchCondition, MonthlyNewCases, List<MonthlyNewCases>, HtmlSimpleTable<MonthlyNewCases>> {

  private static final Map<String, String> PREFECTURE_MAP = prefectureMap();

  @Inject MonthlyNewCasesSimpleTableFacade facade;
  @Inject Logger logger;

  public MonthlyNewCasesSimpleTableView() {
    super(new SearchCondition());
  }

  @Override
  protected SimpleSearchFacade<SearchCondition, List<MonthlyNewCases>, MonthlyNewCases>
      getFacade() {
    return facade;
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }

  @Override
  protected String getDestinationOnFound() {
    return "result.xhtml";
  }

  @Override
  protected boolean isEmpty(List<MonthlyNewCases> searchResult) {
    return searchResult.isEmpty();
  }

  @Override
  protected HtmlSimpleTable<MonthlyNewCases> createHtmlTable(List<MonthlyNewCases> result) {
    return new HtmlSimpleTable<>(result)
        .caption("Covid-19 Monthly New Cases")
        .tableClass("monthlyNewCases")
        .highlight(
            MonthlyNewCasesSimpleTableView::??????????????????,
            MonthlyNewCasesSimpleTableView::??????????????????HighlightClasses)
        .addSequenceColumn("Seq")
        .headerColumn(true)
        .columnClass("seqColumn")
        .headerCellClass("seqHeader")
        .bodyCellClass("seqBody")
        .endColumn()
        .addSimpleColumn("??????", MonthlyNewCases::getYearMonth)
        .converter(HtmlConverters.??????())
        .columnClass("yearMonthColumn")
        .headerCellClass("yearMonthHeader")
        .bodyCellClass("yearMonthBody")
        .endColumn()
        .addSimpleColumn("????????????", MonthlyNewCases::getPrefecture)
        .converter(MonthlyNewCasesSimpleTableView::convertPrefecture)
        .columnClass("????????????Column")
        .headerCellClass("????????????Header")
        .bodyCellClass("????????????Body")
        .highlight(
            MonthlyNewCasesSimpleTableView::??????????????????,
            MonthlyNewCasesSimpleTableView::??????????????????HighlightClasses)
        .endColumn()
        .addSimpleColumn("??????????????????", MonthlyNewCases::getCases)
        .converter(HtmlConverters.??????????????????())
        .columnClass("??????????????????Column")
        .headerCellClass("??????????????????Header")
        .bodyCellClass("??????????????????Body")
        .endColumn();
  }

  @Override
  protected WorkbookModel createWorkbookModel(List<MonthlyNewCases> result) {
    return new WorkbookModel("MonthlyNewCases.xlsx", MonthlyNewCasesSimpleTableView::createStyleMap)
        .addSimpleTable("list", result)
        .headerStyleKey("header")
        .bodyStyleKeys(Arrays.asList("odd", "even"))
        .highlight(
            MonthlyNewCasesSimpleTableView::??????????????????,
            MonthlyNewCasesSimpleTableView::??????????????????HighlightConverter)
        .addCaption("Covid-19")
        .captionStyleKey("caption")
        .endCaption()
        .addSequenceColumn("Seq", AUTO_SIZE)
        .endColumn()
        .addColumn("??????", MonthlyNewCases::getYearMonth, byCharacters(7))
        .bodyStyleKeys(Arrays.asList("yearMonth:odd", "yearMonth:even"))
        .highlightStyleConverter(MonthlyNewCasesSimpleTableView::??????????????????YearMonthHighlightConverter)
        .endColumn()
        .addColumn("????????????", MonthlyNewCases::getPrefecture, byCharacters(8))
        .converter(MonthlyNewCasesSimpleTableView::convertPrefecture)
        .endColumn()
        .addColumn("??????????????????", MonthlyNewCases::getCases, byCharacters(12))
        .bodyStyleKeys(Arrays.asList("??????????????????:odd", "??????????????????:even"))
        .highlightStyleConverter(MonthlyNewCasesSimpleTableView::????????????????????????????????????HighlightConverter)
        .endColumn()
        .endTable();
  }

  private static Map<String, XSSFCellStyle> createStyleMap(XSSFWorkbook workbook) {
    XSSFColor evenFillColor = Colors.create(workbook, 224, 255, 255);
    XSSFColor redColor = Colors.create(workbook, 255, 0, 0);

    XSSFFont captionFont = Fonts.boldOfSize(workbook, 14);
    XSSFFont headerFont = Fonts.bold(workbook);
    XSSFFont redFont = Fonts.ofColor(workbook, redColor);

    XSSFCellStyle standardStyle = workbook.getStylesSource().getStyleAt(0);

    Map<String, XSSFCellStyle> styleMap = new HashMap<>();
    styleMap.put("caption", CellStyles.create(workbook, captionFont));
    styleMap.put("header", CellStyles.create(workbook, headerFont));
    styleMap.put("odd", standardStyle);
    styleMap.put("even", CellStyles.create(workbook, evenFillColor));
    styleMap.put("yearMonth:odd", CellStyles.create(workbook, "yyyy/mm"));
    styleMap.put("yearMonth:even", CellStyles.create(workbook, "yyyy/mm", evenFillColor));
    styleMap.put("??????????????????:odd", CellStyles.create(workbook, "#,##0"));
    styleMap.put("??????????????????:even", CellStyles.create(workbook, "#,##0", evenFillColor));
    styleMap.put("??????????????????:odd", CellStyles.create(workbook, redFont));
    styleMap.put("??????????????????:even", CellStyles.create(workbook, redFont, evenFillColor));
    styleMap.put("??????????????????:yearMonth:odd", CellStyles.create(workbook, "yyyy/mm", redFont));
    styleMap.put(
        "??????????????????:yearMonth:even", CellStyles.create(workbook, "yyyy/mm", redFont, evenFillColor));
    styleMap.put("??????????????????:??????????????????:odd", CellStyles.create(workbook, "#,##0", redFont));
    styleMap.put(
        "??????????????????:??????????????????:even", CellStyles.create(workbook, "#,##0", redFont, evenFillColor));
    return Collections.unmodifiableMap(styleMap);
  }

  private static final Set<String> ??????????????????Set =
      new HashSet<>(Arrays.asList("Hokkaido", "Tokyo", "Osaka", "Okinawa"));

  private static Object ??????????????????(MonthlyNewCases entity) {
    return ??????????????????Set.contains(entity.getPrefecture()) ? Boolean.TRUE : Boolean.FALSE;
  }

  private static String convertPrefecture(String prefecture) {
    return PREFECTURE_MAP.get(prefecture);
  }

  private static String ??????????????????HighlightClasses(Object o) {
    return Boolean.TRUE.equals(o) ? "watching" : null;
  }

  private static List<String> ??????????????????HighlightConverter(Object o) {
    return Boolean.TRUE.equals(o) ? Arrays.asList("??????????????????:odd", "??????????????????:even") : null;
  }

  private static List<String> ??????????????????YearMonthHighlightConverter(Object o) {
    return Boolean.TRUE.equals(o)
        ? Arrays.asList("??????????????????:yearMonth:odd", "??????????????????:yearMonth:even")
        : null;
  }

  private static List<String> ????????????????????????????????????HighlightConverter(Object o) {
    return Boolean.TRUE.equals(o) ? Arrays.asList("??????????????????:??????????????????:odd", "??????????????????:??????????????????:even") : null;
  }

  private static Map<String, String> prefectureMap() {
    Map<String, String> map = new HashMap<>(64);
    map.put("ALL", "??????");
    map.put("Aichi", "??????");
    map.put("Akita", "??????");
    map.put("Aomori", "??????");
    map.put("Chiba", "??????");
    map.put("Ehime", "??????");
    map.put("Fukui", "??????");
    map.put("Fukuoka", "??????");
    map.put("Fukushima", "??????");
    map.put("Gifu", "??????");
    map.put("Gunma", "??????");
    map.put("Hiroshima", "??????");
    map.put("Hokkaido", "?????????");
    map.put("Hyogo", "??????");
    map.put("Ibaraki", "??????");
    map.put("Ishikawa", "??????");
    map.put("Iwate", "??????");
    map.put("Kagawa", "??????");
    map.put("Kagoshima", "?????????");
    map.put("Kanagawa", "?????????");
    map.put("Kochi", "??????");
    map.put("Kumamoto", "??????");
    map.put("Kyoto", "??????");
    map.put("Mie", "??????");
    map.put("Miyagi", "??????");
    map.put("Miyazaki", "??????");
    map.put("Nagano", "??????");
    map.put("Nagasaki", "??????");
    map.put("Nara", "??????");
    map.put("Niigata", "??????");
    map.put("Oita", "??????");
    map.put("Okayama", "??????");
    map.put("Okinawa", "??????");
    map.put("Osaka", "??????");
    map.put("Saga", "??????");
    map.put("Saitama", "??????");
    map.put("Shiga", "??????");
    map.put("Shimane", "??????");
    map.put("Shizuoka", "??????");
    map.put("Tochigi", "??????");
    map.put("Tokushima", "??????");
    map.put("Tokyo", "??????");
    map.put("Tottori", "??????");
    map.put("Toyama", "??????");
    map.put("Wakayama", "?????????");
    map.put("Yamagata", "??????");
    map.put("Yamaguchi", "??????");
    map.put("Yamanashi", "??????");
    return unmodifiableMap(map);
  }
}
