
      var tabs = ['Atıf', 'Görüntüleme', 'İndirilme'];
      var defaultTab = 'İndirilme';
      var senateData = [
        {
          type: 'Atıf',
          categories: ['Dergipark', 'TRDizin', 'Sobiad'],
          values: [52, 46, 15],
          colors: ['#D32F2F', '#1976D2', '#dddecc']
        },
        { 
		type: 'Görüntüleme', 
		categories: ['Dergipark', 'TRDizin','Sobiad'], 
		values: [25, 257, 214], 
		colors: ['#EF5350', '#42A5F5','#FFFF00'] },
        {
          type: 'İndirilme',
         	categories: ['Dergipark', 'TRDizin','Sobiad'], 
		values: [267, 255, 214], 
          colors: ['#FFA000', '#FFEE58', '#8BC34A']
        }
      ];

      var config = {
        type: 'radar marker',
        legend: {
          template: '%icon %name: (<b>%pointCount</b>)',
          defaultEntry: {
            cursor: 'default',
            events_click: function() {
              return false;
            }
          }
        },
        defaultAxis_visible: false,
        defaultPoint_marker: { outline_width: 0 }
      };

      var senateChart = JSC.chart(
        'senateChartDiv',
        JSC.merge(
          {
            title_label: {
              text: 'Yayının İstatistikleri',
              style_fontSize: 20,
              margin_bottom: 40
            },
            defaultSeries: {
              mouseTracking_enabled: false,
              shape_size: 390,
              angle: { sweep: 270, start: 135 }
            },
            annotations: [
              {
                label: {
                  text: 'İstatistik',
                  style_fontSize: 26,
                  margin_bottom: 20
                },
                position: 'inside bottom'
              }
            ],
            legend_position: 'inside top',

            yAxis: { scale_range: [-6, 5] },
            defaultPoint: {
              marker: { type: 'circle', size: 16 }
            },
            series: makeSeries(senateData, defaultTab, 5),
            toolbar_items: {
              export_visible: true,
              caseTypes: {
                items: generateTabs(),
                value: defaultTab, //'Atıf',
                position: 'inside top left',
                boxVisible: false,
                label_text: '',
                width: 280,
                offset: '0,-48',
                margin: 5,
                itemsBox: {
                  layout: 'horizontal',
                  visible: true,
                  margin_top: 5
                },
                defaultItem: {
                  type: 'radio',
                  position: 'top',
                  icon_visible: false,
                  padding: [6, 12, 6, -8],
                  margin: 0,
                  outline_width: 0,
                  fill: '#F5F5F5',
                  label: { color: '#BDBDBD', style_fontSize: '20px' },
                  states_select: { label_color: '#424242' },
                  states_hover: { label_color: '#424242' }
                },
                events: {
                  change: function(val) {
                    senateChart.options({ series: makeSeries(senateData, val, 5) });
                  }
                }
              }
            }
          },
          config
        )
      );

     
      function makeSeries(data, type, chartHeight) {
        var typeData = data.filter(function(item) {
          return item.type === type;
        })[0];
        var series = [];
        var sum = 0;
        var ranges = [];
        typeData.categories.forEach(function(category, i) {
          var result = {
            name: category,
            color: typeData.colors[i],
            points: []
          };
          if (category === 'Vacant') {
            result.defaultPoint_marker_outline = { width: 2, color: '#E0E0E0' };
          }
          series.push(result);
          ranges.push([sum + 1, (sum += typeData.values[i])]);
        });

        for (var i = 0; i < Math.ceil(sum / chartHeight); i++) {
          for (var j = 0; j < chartHeight; j++) {
            var index = i + j + (chartHeight - 1) * i,
              seriesIndex = mapValue(ranges, index + 1);
            if (index <= sum - 1) {
              series[seriesIndex].points.push({ x: i, y: j });
            } else break;
          }
        }
        return series;

        function mapValue(ranges, value) {
          return ranges.indexOf(
            ranges.find(function(item) {
              return value >= item[0] && value <= item[1];
            })
          );
        }
      }

      function generateTabs() {
        var buttons = {};
        tabs.forEach(function(item, i) {
          buttons[item] = { label_text: item };
        });
        return buttons;
      }
    

