#! /usr/bin/env python
# coding=utf-8
# get image manjisweet()

import redis
import matplotlib.pyplot as plt

print('start...')
print('rds-db1')
pool = redis.ConnectionPool(host='x.x.x.x', port=36379, db=1, password="password")
r = redis.Redis(connection_pool=pool)

print("get name's keys: ")
keys = r.hkeys("App::User::Click")
print(keys)

print("get keys' values")
hvals = r.hvals("App::User::Click")
print(hvals)

u1 = int(str(r.hget("App::User::Click","u1"))[2:-1])
u2 = int(str(r.hget("App::User::Click","u2"))[2:-1])
u3 = int(str(r.hget("App::User::Click","u3"))[2:-1])
u4 = int(str(r.hget("App::User::Click","u4"))[2:-1])
u5 = int(str(r.hget("App::User::Click","u5"))[2:-1])
u6 = int(str(r.hget("App::User::Click","u6"))[2:-1])
u7 = int(str(r.hget("App::User::Click","u7"))[2:-1])
u8 = int(str(r.hget("App::User::Click","u8"))[2:-1])
u9 = int(str(r.hget("App::User::Click","u9"))[2:-1])
u10 = int(str(r.hget("App::User::Click","u10"))[2:-1])


values_list = [u1,u2,u3,u4,u5,u6,u7,u8,u9,u10]
print(values_list)
plt.bar(range(len(keys)), values_list, color='gcb')
plt.show()
