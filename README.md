# exchange-02-2017
[![Build Status](https://travis-ci.org/algys/exchange-02-2017.svg?branch=master)](https://travis-ci.org/algys/exchange-02-2017)

#Описание Игры
Суть игры построить наиболее многочисленную популяцию.
У игрока изначально есть одна башня с населением в 100 солдат.
Игра идет по-шагово, за каждый ход игрок может построить новую башню и перенести туда половину населения или просто перейти в свою соседнюю башню с переносом туда половины населения. За бездействие штраф 10 солдат. 
Все игроки совершают ход одновременно через определенный промежуток времени, в течении которого они могут обдумать следующий ход.

#Команда
* Сергей Говязин
* Алгыс Иевлев
* Алексей Набережный
* Динияр Кадырбеков

#Ссыллка на API
  https://cyclic-server-api-test.herokuapp.com/api

#Описание REST API

##1.Регистрация

  path = /api/user, method = PUT
  
  Входных данные: 
  
    Обязательные поля: login, password, email
    
    Необязательные: firstName, lastName
           
  Ответ:
    
    {"status": "..."}
    
##2.Изменение данных

  path = /api/user, method = POST
  
  Входных данные: 
    
    Необязательные поля: login, password, email, firstName, lastName
       
  Ответ:
    
    {"status": "..."}
    
##3.Полученние данных о текущем пользователе

  path = /api/user, method = GET
  
  Ответ:
    
    {
      "id": "...",
      "login": "...",
      "email": "...",
      "firstName": "...",
      "lastName": "...",
    }
    
##4.Полученние данных о пользователе по id

  path = /api/user/{id}, method = GET
       
  Ответ:
    
    {
      "id": "...",
      "login": "...",
      "firstName": "...",
      "lastName": "...",
    }
    
##5.Вход

  path = /api/login, method = POST
  
  Входных данные: 
    
    Обязательные поля: login, password
   
  Ответ:
    
    {"status": "..."}
    
##6.Выход

  path = /api/login, method = DELETE
   
  Ответ:
    
    {"status": "..."}





