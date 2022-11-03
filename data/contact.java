package client

import (
	"CS_TWO/dao"
	"fmt"
)

func out(book dao.Book) {
	fmt.Printf("id:%d  姓名：%s 电话号码：%s 地址: %s\n", book.Id, book.Name, book.PhoneNum, book.Address)
}