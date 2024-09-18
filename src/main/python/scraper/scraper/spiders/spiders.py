import json
import scrapy
import re


class BreedPageSpider(scrapy.Spider):
    name = 'breed_pages'
    start_urls = ['https://www.petguide.com/breeds/rabbit',
                  'https://www.petguide.com/breeds/bird',
                  'https://www.petguide.com/breeds/fish',
                  'https://www.petguide.com/breeds/horse',
                  'https://www.petguide.com/breeds/turtle']

    def parse(self, response):
        for item in response.css('.card-title a::attr(href)'):
            if re.fullmatch('^/breeds/.+/.+/', item.get()) is not None:
                yield {
                    'url': item.get()
                }
            else:
                continue
        next_page = response.css('.sl-next-page').attrib['href']
        if next_page is not None:
            next_page = response.urljoin(next_page)
            yield scrapy.Request(next_page, callback=self.parse)


class TableInfoSpider(scrapy.Spider):
    name = 'table_info'
    start_urls = [f"https://www.petguide.com{item['url']}" for item in json.load(open('pages.json'))]

    def parse(self, response):
        item_dict = {'name': response.css('.js-activity-title').xpath('//h1/text()').get()}
        for item in response.css('.item'):
            item_elems = item.css('div::text').getall()
            if len(item_elems) == 0:
                continue
            item_dict[item_elems[1]] = item_elems[3]
        item_dict['img-src'] = response.css('.media-item-track')[0].xpath('@src').get()
        yield item_dict
