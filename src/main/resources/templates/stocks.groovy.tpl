yieldUnescaped '<!DOCTYPE html>'
html {
    head {
        title('Stock Prices')
        meta('http-equiv':"Content-Type", content:"text/html; charset=UTF-8")
        meta('http-equiv':"Content-Style-Type", 'content':"text/css")
        meta('http-equiv':"Content-Script-Type", 'content':"text/javascript")
        link('rel':"shortcut icon", 'href':"/images/favicon.ico")
        link('rel':"stylesheet", 'type':"text/css", 'href':"/css/style.css", 'media':"all")
yieldUnescaped '''<script type="text/javascript" src="/js/util.js"></script>
<style type="text/css">
/*<![CDATA[*/
body {
	color: #333333;
	line-height: 150%;
}

thead {
	font-weight: bold;
	background-color: #CCCCCC;
}

.odd {
	background-color: #FFCCCC;
}

.even {
	background-color: #CCCCFF;
}

.minus {
	color: #FF0000;
}

/*]]>*/
</style>'''

    }
    body {
        h1('Stock Prices')

        table {
            thead {
                tr {
                    th('#')
                    th('symbol')
                    th('name')
                    th('price')
                    th('change')
                    th('ratio')
                }
            }
                tbody {
            items.eachWithIndex {item, idx ->

                    tr (class: idx % 2 ? 'even' : 'odd'){
                        td(idx + 1)
                        td {
                            a(item.symbol, href: '/stocks/' + item.symbol)
                        }
                        td {
                            a(item.name, href: item.url)
                        }
                        td {
                            strong(item.price)
                        }
                        if (item.change < 0) {
                            td(item.change, class: 'minus')
                            td(item.ratio, class: 'minus')
                        } else {
                            td(item.change)
                            td(item.ratio)
                        }
                    }
            }
                            }
        }
    }
}