package com.gmail.kasabuta4.jsfdemo.covid19.view;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesSummary;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

class LineChartModelBuilder {

  private static final DateTimeFormatter YEAR_MONTH_FORMATTER =
      DateTimeFormatter.ofPattern("uuuu-MM");

  private static final Map<String, String> COLOR_MAP;

  private final MonthlyNewCasesSummary summary;

  LineChartModelBuilder(MonthlyNewCasesSummary summary) {
    this.summary = summary;
  }

  LineChartModel build() {
    return buildChartModel();
  }

  private LineChartModel buildChartModel() {
    LineChartModel model = new LineChartModel();
    model.setOptions(buildChartOptions());
    model.setData(buildChartData());
    return model;
  }

  private LineChartOptions buildChartOptions() {
    LineChartOptions options = new LineChartOptions();
    options.setTitle(buildChartTitle());
    return options;
  }

  private Title buildChartTitle() {
    Title title = new Title();
    title.setDisplay(true);
    title.setText("都道府県別月間感染者数");
    return title;
  }

  private ChartData buildChartData() {
    ChartData data = new ChartData();
    summary
        .getPrefectures()
        .stream()
        .map(this::buildLineChartDataSet)
        .forEach(data::addChartDataSet);
    data.setLabels(yearMonthStringList());
    return data;
  }

  private LineChartDataSet buildLineChartDataSet(String prefecture) {
    LineChartDataSet dataSet = new LineChartDataSet();
    dataSet.setLabel(prefecture);
    dataSet.setData(monthlyNewCasesList(prefecture));
    dataSet.setFill(true);
    dataSet.setBorderColor(COLOR_MAP.get(prefecture));
    dataSet.setLineTension(0);
    return dataSet;
  }

  private List<String> yearMonthStringList() {
    return summary.getYearMonths().stream().map(YEAR_MONTH_FORMATTER::format).collect(toList());
  }

  private List<Object> monthlyNewCasesList(String prefecture) {
    Map<YearMonth, Integer> map = summary.getTable().get(prefecture);
    return summary.getYearMonths().stream().map(map::get).collect(toList());
  }

  static {
    Map<String, String> map = new HashMap<>(64);
    map.put("Aichi", "#0FF");
    map.put("Akita", "#0FC");
    map.put("Aomori", "#0F9");
    map.put("Chiba", "#0F6");
    map.put("Ehime", "#0F3");
    map.put("Fukui", "#0F0");
    map.put("Fukuoka", "#0CF");
    map.put("Fukushima", "#0CC");
    map.put("Gifu", "#0C9");
    map.put("Gunma", "#0C6");
    map.put("Hiroshima", "#0C3");
    map.put("Hokkaido", "#0C0");
    map.put("Hyogo", "#09F");
    map.put("Ibaraki", "#09C");
    map.put("Ishikawa", "#099");
    map.put("Iwate", "#096");
    map.put("Kagawa", "#093");
    map.put("Kagoshima", "#090");
    map.put("Kanagawa", "#06F");
    map.put("Kochi", "#06C");
    map.put("Kumamoto", "#069");
    map.put("Kyoto", "#066");
    map.put("Mie", "#063");
    map.put("Miyagi", "#060");
    map.put("Miyazaki", "#03F");
    map.put("Nagano", "#03C");
    map.put("Nagasaki", "#039");
    map.put("Nara", "#036");
    map.put("Niigata", "#033");
    map.put("Oita", "#030");
    map.put("Okayama", "#00F");
    map.put("Okinawa", "#00C");
    map.put("Osaka", "#009");
    map.put("Saga", "#006");
    map.put("Saitama", "#003");
    map.put("Shiga", "#000");
    map.put("Shimane", "#66F");
    map.put("Shizuoka", "#66C");
    map.put("Tochigi", "#669");
    map.put("Tokushima", "#666");
    map.put("Tokyo", "#663");
    map.put("Tottori", "#660");
    map.put("Toyama", "#60F");
    map.put("Wakayama", "#60C");
    map.put("Yamagata", "#609");
    map.put("Yamaguchi", "#606");
    map.put("Yamanashi", "#603");
    COLOR_MAP = unmodifiableMap(map);
  }
}
