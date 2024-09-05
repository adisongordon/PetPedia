from typing import Any
import json

import scrapy
from scrapy.http import Response

class BreedPageSpider(scrapy.Spider):
    name = 'breed_pages'
    start_urls = ['https://www.petguide.com/breeds/rabbit',
                  'https://www.petguide.com/breeds/bird',
                  'https://www.petguide.com/breeds/fish',
                  'https://www.petguide.com/breeds/horse',
                  'https://www.petguide.com/breeds/turtle']

    def parse(self, response):
        for item in response.css('.card-title a::attr(href)'):
            yield {
                'url': item.get()
            }


class TableInfoSpider(scrapy.Spider):
    name = 'table_info'
    start_urls = [f"https://www.petguide.com{item['url']}" for item in json.load(open('pages.json'))] #['https://www.petguide.com/breeds/rabbit/florida-white-rabbit/']

    def parse(self, response):
        for item in response.css('.item'):
            item_elems = item.css('div::text').getall()
            if len(item_elems) == 0:
                pass
            yield {
                item_elems[1]: item_elems[3]
            }
