function revenuaByMonthChart(data) {
  var MonthArr = [];
  data.forEach(element => {
    MonthArr.push({ time: "Tháng " + element[1], value: element[0] })
  });
  initRevenuaChart(MonthArr);
}

function revenuaByDayOfWeekChart(data) {
  var DayOfWeekArr = [];
  data.forEach(element => {
    var dayName = element[1];
    switch (dayName) {
      case 1:
        DayOfWeekArr.push({ time: "Chủ nhật", value: element[0] });
        break;
      default:
        DayOfWeekArr.push({ time: "Thứ " + element[1], value: element[0] });
        break;
    }
  })
  initRevenuaChart(DayOfWeekArr);
}

function orderStatusByMonthChart(data) {
  var orderStatusArr = [];
  $.each(data, function (key, val) {
    orderStatusArr.push({ time: "Tháng " + key, SHIPPED: val.SHIPPED, SHIPMENT_FAILED: val.SHIPMENT_FAILED, CANCELED: val.CANCELED, REJECTED: val.REJECTED })
  });
  initOrderStatusChart(orderStatusArr)
}

function orderStatusByDayOkWeekChart(data) {
  var orderStatusArr = [];
  $.each(data, function (key, val) {
    var dayName = key;
    var time;
    switch (dayName) {
      case "1":
        time = "Chủ nhật";
        break;
      default:
        time = "Thứ " + key;
        break;
    }
    orderStatusArr.push({ time: time, SHIPPED: val.SHIPPED, SHIPMENT_FAILED: val.SHIPMENT_FAILED, CANCELED: val.CANCELED, REJECTED: val.REJECTED })
  });
  initOrderStatusChart(orderStatusArr)
}


function initRevenuaChart(dataArr) {
  am5.ready(function () {
    // Create root element
    // https://www.amcharts.com/docs/v5/getting-started/#Root_element
    var root = am5.Root.new("revenua-chart");

    // Set themes
    // https://www.amcharts.com/docs/v5/concepts/themes/
    root.setThemes([
      am5themes_Animated.new(root)
    ]);

    // Create chart
    // https://www.amcharts.com/docs/v5/charts/xy-chart/
    var chart = root.container.children.push(am5xy.XYChart.new(root, {
      panX: false,
      panY: false,
      wheelX: "none",
      wheelY: "none",
      layout: root.verticalLayout
    }));
    // Data
    var data = dataArr;

    // Populate data
    for (var i = 0; i < (data.length - 1); i++) {
      data[i].valueNext = data[i + 1].value;
    }

    // Create axes
    // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
    var xRenderer = am5xy.AxisRendererX.new(root, {
      cellStartLocation: 0.1,
      cellEndLocation: 0.9,
      minGridDistance: 30
    });

    var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
      categoryField: "time",
      renderer: xRenderer,
      tooltip: am5.Tooltip.new(root, {})
    }));

    xRenderer.grid.template.setAll({
      location: 1
    })

    xAxis.data.setAll(data);

    var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
      min: 0,
      renderer: am5xy.AxisRendererY.new(root, {
        strokeOpacity: 0.1
      })
    }));

    // Add series
    // https://www.amcharts.com/docs/v5/charts/xy-chart/series/

    // Column series
    var series = chart.series.push(am5xy.ColumnSeries.new(root, {
      xAxis: xAxis,
      yAxis: yAxis,
      valueYField: "value",
      categoryXField: "time"
    }));

    series.columns.template.setAll({
      tooltipText: "{categoryX}: {valueY}",
      width: am5.percent(90),
      tooltipY: 0
    });

    series.data.setAll(data);

    // Variance indicator series
    var series2 = chart.series.push(am5xy.ColumnSeries.new(root, {
      xAxis: xAxis,
      yAxis: yAxis,
      valueYField: "valueNext",
      openValueYField: "value",
      categoryXField: "time",
      fill: am5.color(0x555555),
      stroke: am5.color(0x555555)
    }));

    series2.columns.template.setAll({
      width: 1
    });

    series2.data.setAll(data);

    series2.bullets.push(function () {
      var label = am5.Label.new(root, {
        text: "{valueY}",
        fontWeight: "500",
        fill: am5.color(0x00cc00),
        centerY: am5.p100,
        centerX: am5.p50,
        populateText: true
      });

      // Modify text of the bullet with percent
      label.adapters.add("text", function (text, target) {
        var percent = getVariancePercent(target.dataItem);
        return percent ? percent + "%" : text;
      });

      // Set dynamic color of the bullet
      label.adapters.add("centerY", function (center, target) {
        return getVariancePercent(target.dataItem) < 0 ? 0 : center;
      });

      // Set dynamic color of the bullet
      label.adapters.add("fill", function (fill, target) {
        return getVariancePercent(target.dataItem) < 0 ? am5.color(0xcc0000) : fill;
      });

      return am5.Bullet.new(root, {
        locationY: 1,
        sprite: label
      });
    });

    series2.bullets.push(function () {
      var arrow = am5.Graphics.new(root, {
        rotation: -90,
        centerX: am5.p50,
        centerY: am5.p50,
        dy: 3,
        fill: am5.color(0x555555),
        stroke: am5.color(0x555555),
        draw: function (display) {
          display.moveTo(0, -3);
          display.lineTo(8, 0);
          display.lineTo(0, 3);
          display.lineTo(0, -3);
        }
      });

      arrow.adapters.add("rotation", function (rotation, target) {
        return getVariancePercent(target.dataItem) < 0 ? 90 : rotation;
      });

      arrow.adapters.add("dy", function (dy, target) {
        return getVariancePercent(target.dataItem) < 0 ? -3 : dy;
      });

      return am5.Bullet.new(root, {
        locationY: 1,
        sprite: arrow
      })
    })

    // Make stuff animate on load
    // https://www.amcharts.com/docs/v5/concepts/animations/
    series.appear();
    chart.appear(1000, 100);

    function getVariancePercent(dataItem) {
      if (dataItem) {
        var value = dataItem.get("valueY");
        var openValue = dataItem.get("openValueY");
        var change = value - openValue;
        return Math.round(change / openValue * 100);
      }
      return 0;
    }

  }); // end am5.ready()
}

function initOrderStatusChart(data1) {
  am5.ready(function () {
    // Create root element
    // https://www.amcharts.com/docs/v5/getting-started/#Root_element
    var root = am5.Root.new("order-status-chart");

    // Set themes
    // https://www.amcharts.com/docs/v5/concepts/themes/
    root.setThemes([
      am5themes_Animated.new(root)
    ]);

    // Create chart
    // https://www.amcharts.com/docs/v5/charts/xy-chart/
    var chart = root.container.children.push(am5xy.XYChart.new(root, {
      panX: false,
      panY: false,
      maxZoomLevel: 1,
      layout: root.verticalLayout
    }));
    //color for columns
    // chart.get("colors").set("colors", [
    //     //am5.color(0x68dc76),
    //     //am5.color(0xb30000),
    //    // am5.color("#ffc107"),
    //    // am5.color("#a817b8")
    // ]);

    // Add legend
    // https://www.amcharts.com/docs/v5/charts/xy-chart/legend-xy-series/
    var legend = chart.children.push(
      am5.Legend.new(root, {
        centerX: am5.p50,
        x: am5.p50
      })
    );

    var data = data1

    // Create axes
    // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
    var xRenderer = am5xy.AxisRendererX.new(root, {
      cellStartLocation: 0.1,
      cellEndLocation: 0.9,
      minGridDistance: 30
    });
    xRenderer.labels.template.setAll({
      rotation: 0,
      centerY: am5.p50,
      centerX: am5.p50,
      paddingRight: 10,
      paddingLeft: 10
    });

    var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
      categoryField: "time",
      renderer: xRenderer,
      tooltip: am5.Tooltip.new(root, {})
    }));

    xRenderer.grid.template.setAll({
      location: 1
    })

    xAxis.data.setAll(data);

    var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
      renderer: am5xy.AxisRendererY.new(root, {
        strokeOpacity: 0.1
      })
    }));

    // Add series
    // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
    function makeSeries(name, fieldName) {
      var series = chart.series.push(am5xy.ColumnSeries.new(root, {
        name: name,
        xAxis: xAxis,
        yAxis: yAxis,
        valueYField: fieldName,
        categoryXField: "time"
      }));
      series.columns.template.setAll({
        tooltipText: "{name}, {categoryX} - {valueY} đơn hàng",
        width: am5.percent(80),
        tooltipY: 0,
        strokeOpacity: 0
      });
      series.data.setAll(data);

      // Make stuff animate on load
      // https://www.amcharts.com/docs/v5/concepts/animations/
      series.appear();

      series.bullets.push(function () {
        return am5.Bullet.new(root, {
          locationY: 0,
          sprite: am5.Label.new(root, {
            text: "{valueY}",
            fill: root.interfaceColors.get("alternativeText"),
            centerY: 0,
            centerX: am5.p50,
            populateText: true
          })
        });
      });
      legend.data.push(series);
    }
    makeSeries("Giao hàng thành công ", "SHIPPED");
    makeSeries("Giao hàng thất bại", "SHIPMENT_FAILED");
    makeSeries("Khách hủy đơn", "CANCELED");
    makeSeries("Shop hủy đơn", "REJECTED");
    // Make stuff animate on load
    // https://www.amcharts.com/docs/v5/concepts/animations/
    chart.appear(1000, 100);
  }); // end am5.ready()
}

function initBestSellingProductChart(data) {
  var dataArr = [];
  data.forEach(element => {
    dataArr.push({ product: element[3], revenua: element[1], unitSold: element[2], icon: element[4] })
  });
  am5.ready(function () {
    // Create root element
    // https://www.amcharts.com/docs/v5/getting-started/#Root_element
    var root = am5.Root.new("best-selling-product-chart");

    // Set themes
    // https://www.amcharts.com/docs/v5/concepts/themes/
    root.setThemes([
      am5themes_Animated.new(root)
    ]);

    // Create chart
    // https://www.amcharts.com/docs/v5/charts/xy-chart/
    var chart = root.container.children.push(am5xy.XYChart.new(root, {
      panX: false,
      panY: false,
      maxZoomLevel: 1,
      layout: root.verticalLayout
    }));

    // Add cursor
    // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
    var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
      behavior: "none"
    }));

    var data = dataArr;

    // Create axes
    // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
    var xRenderer = am5xy.AxisRendererX.new(root, {
      minGridDistance: 30
    })

    var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
      categoryField: "product",
      renderer: xRenderer,
      bullet: function (root, axis, dataItem) {
        return am5xy.AxisBullet.new(root, {
          location: 0.5,
          sprite: am5.Picture.new(root, {
            width: 35,
            height: 35,
            centerY: am5.p50,
            centerX: am5.p50,
            src: dataItem.dataContext.icon
          })
        });
      }
    }));

    xRenderer.grid.template.setAll({
      location: 1
    })
    // Enable label wrapping, font size, etc
    xRenderer.labels.template.setAll({
      paddingTop: 20,
      fontSize: 12,
      oversizedBehavior: "wrap",
      textAlign: "center"
    });
    // Set up automatic width calculation using an adapter
    xRenderer.labels.template.adapters.add("width", function (width, target) {
      var x0 = xAxis.getDataItemCoordinateY(xAxis.dataItems[0], "category", 0);
      var x1 = xAxis.getDataItemCoordinateY(xAxis.dataItems[0], "category", 1);
      target.set("maxWidth", x1 - x0)
      return x1 - x0;
    });

    xAxis.data.setAll(data);

    var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
      renderer: am5xy.AxisRendererY.new(root, {
        strokeOpacity: 0.1
      })
    }));

    var yAxis2 = chart.yAxes.push(am5xy.ValueAxis.new(root, {
      renderer: am5xy.AxisRendererY.new(root, {
        strokeOpacity: 0.1,
        opposite: true
      })
    }));

    // Add series
    // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
    var series = chart.series.push(am5xy.ColumnSeries.new(root, {
      xAxis: xAxis,
      yAxis: yAxis,
      valueYField: "revenua",
      categoryXField: "product",
      tooltip: am5.Tooltip.new(root, {
        labelText: "Doanh thu : {valueY}"
      })
    }));

    var unitSoldSeries = chart.series.push(am5xy.LineSeries.new(root, {
      xAxis: xAxis,
      yAxis: yAxis2,
      valueYField: "unitSold",
      categoryXField: "product",
      stroke: chart.get("colors").getIndex(3),
      fill: chart.get("colors").getIndex(3),
      tooltip: am5.Tooltip.new(root, {
        labelText: "Đã bán : {valueY} "
      })
    }));

    unitSoldSeries.strokes.template.setAll({ strokeWidth: 2 });
    unitSoldSeries.bullets.push(function () {
      var graphics = am5.Rectangle.new(root, {
        width: 10,
        height: 10,
        centerX: am5.p50,
        centerY: am5.p50,
        fill: unitSoldSeries.get("stroke")
      });
      return am5.Bullet.new(root, {
        sprite: graphics
      });
    });
    unitSoldSeries.data.setAll(data);
    series.data.setAll(data);

    // Make stuff animate on load
    // https://www.amcharts.com/docs/v5/concepts/animations/

    series.appear();
    chart.appear(1000, 100);

  }); // end am5.ready()
}