import requests
import pandas as pd
from bs4 import BeautifulSoup as bs
import lxml
import csv
import re 





def scraper():
    combined_list=[]
    for page in range(1, 29):
        list = players(fr"C:\Users\alex\Documents\pitonas\fifa\html_source2\Players - FIFA 16 - Sep 22, 2016 _ SoFIFA{page}.html")
        combined_list.extend(list)
    
    return combined_list
        
        
        



def players(file):
    list=[]
    with open(file, "r", encoding="utf-8") as f:
        html_content = f.read()
    soup = bs(html_content, 'lxml')
    

    rows = soup.find_all('tr')[1:]
    for row in rows:
        cols =  row.find_all('td')
        data = [c.get_text(strip=True) for c in cols]
        sel_data = [data[i] for i in [1, 3, 4, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16]]


        sel_data[2]=sel_data[2][:-11]
        name = re.sub(r'[A-Z]+$', '', sel_data[0])
        sel_data[0]=name


        list.append(sel_data)

    return list
    


if __name__ == "__main__":
    list = scraper()

    file_path = r"C:\Users\alex\Documents\pitonas\fifa\players.csv"

    with open(file_path, "w",  newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerows(list)
    print("done")