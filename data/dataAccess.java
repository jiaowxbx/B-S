package client

const PAGESIZE = 10

type UI struct{}

func (t *UI) Start(db *gorm.DB) {
	fmt.Println("请选择你要进行的操作")
	fmt.Println("1.获取通讯列表")
	fmt.Println("2.通过名字查询信息")
	fmt.Println("3.添加一个通讯录")
	choose1 := 0
	_, err := fmt.Scanf("%d\n", &choose1)
	if err != nil {
		t.startErr(db)
	}
	switch choose1 {
	case 1:
		t.list(db, 1)
	case 2:
		t.findByName(db, 1)
	case 3:
		t.save(db)
	default:
		t.startErr(db)
	}

	return
}

func (t *UI) startErr(db *gorm.DB) {
	fmt.Println("输入错误，请重新输入")
	choose1 := 0
	_, err := fmt.Scanf("%d\n", &choose1)
	if err != nil {
		t.startErr(db)
	}
	switch choose1 {
	case 1:
		t.list(db, 1)
	case 2:
		t.findByName(db, 1)
	case 3:
		t.save(db)
	default:
		t.startErr(db)
	}
}

func (t *UI) list(db *gorm.DB, pageNum int) {
	book := dao.Book{}
	lists, count, err := book.FindList(db, pageNum, PAGESIZE)
	if err != nil {
		fmt.Println("发生错误，返回上一级")
		t.Start(db)
		return
	}

	fmt.Printf("共找到%d条记录，当前位于%d页，共%d页\n", count, pageNum, count/PAGESIZE+1)
	for _, list := range lists {
		out(list)
	}
	fmt.Println()
	fmt.Println("1.下一页 2.上一页 3.删除内容 4.修改内容 5.新增内容 6.通过姓名查询")
	choose := 0
	fmt.Scanf("%d\n", &choose)
	if err != nil {
		fmt.Println("输入错误")
		t.list(db, pageNum)
	}
	switch choose {
	case 1:
		t.list(db, pageNum+1)
	case 2:
		t.list(db, pageNum-1)
	case 3:
		t.delete(db)
	case 4:
		t.update(db)
	case 5:
		t.save(db)
	case 6:
		t.findByName(db, 1)
	default:
		t.Start(db)
	}

}

func (t *UI) findByName(db *gorm.DB, pageNum int) {
	fmt.Println("请输入名字：")

	book := dao.Book{}
	_, err := fmt.Scanf("%s\n", &book.Name)
	if err != nil {
		fmt.Println("输入错误")
		t.findByName(db, pageNum)
	}

	lists, count, err := book.FindList(db, pageNum, PAGESIZE)
	if err != nil {
		fmt.Println("发生错误，返回上一级")
		t.Start(db)
		return
	}

	fmt.Printf("共找到%d条记录，当前位于%d页，共%d页\n", count, pageNum, count/PAGESIZE+1)
	for _, list := range lists {
		out(list)
	}

	fmt.Println()
	fmt.Println("1.下一页 2.上一页 3.删除内容 4.修改内容 5.新增内容 6.返回初始页")

	choose := 0
	_, err = fmt.Scanf("%d\n", &choose)
	if err != nil {
		fmt.Println("输入错误")
		t.list(db, pageNum)
	}
	switch choose {
	case 1:
		t.findByNameNext(db, pageNum+1, book)
	case 2:
		t.findByNameNext(db, pageNum-1, book)
	case 3:
		t.delete(db)
	case 4:
		t.update(db)
	case 5:
		t.save(db)
	case 6:
		t.Start(db)
	default:
		t.Start(db)
	}
}

func (t *UI) findByNameNext(db *gorm.DB, pageNum int, book dao.Book) {

	lists, count, err := book.FindList(db, pageNum, PAGESIZE)
	if err != nil {
		fmt.Println("发生错误，返回上一级")
		t.Start(db)
		return
	}

	fmt.Printf("共找到%d条记录，当前位于%d页，共%d页\n", count, pageNum, count/PAGESIZE+1)
	for _, list := range lists {
		out(list)
	}
	fmt.Println()
	fmt.Println("1.下一页 2.上一页 3.删除内容 4.修改内容 5.新增内容 6.返回初始页")
	choose := 0
	_, err = fmt.Scanf("%d\n", &choose)
	if err != nil {
		fmt.Println("输入错误")
		t.list(db, pageNum)
	}
	switch choose {
	case 1:
		t.findByNameNext(db, pageNum+1, book)
	case 2:
		t.findByNameNext(db, pageNum-1, book)
	case 3:

	default:
		t.Start(db)
	}
}

func (t *UI) delete(db *gorm.DB) {
	fmt.Println("请输入要删除通讯录的id")
	book := dao.Book{}
	_, err := fmt.Scanf("%d\n", &book.Id)
	if err != nil {
		fmt.Println("输入错误")
		t.delete(db)
	}
	err = book.Delete(db)
	if err != nil {
		fmt.Println("删除错误，返回首页\n")

	} else {
		fmt.Println("删除成功，返回首页\n")
	}
	t.Start(db)
}

func (t *UI) save(db *gorm.DB) {
	fmt.Println("请输入要新增的信息")
	fmt.Println("姓名 电话号码 地址")
	book := dao.Book{}
	_, err := fmt.Scanf("%s %s %s", &book.Name, &book.PhoneNum, &book.Address)
	if err != nil {
		fmt.Println("输入错误")
		t.save(db)
	}
	err = book.Save(db)
	if err != nil {
		fmt.Println("保存失败，返回首页")
	} else {
		fmt.Println("保存成功，返回首页")
	}
	t.Start(db)
}

func (t *UI) update(db *gorm.DB) {
	fmt.Println("请输入要修改的信息")
	fmt.Println("id 姓名 电话号码 地址")
	book := dao.Book{}
	_, err := fmt.Scanf("%d %s %s %s", &book.Id, &book.Name, &book.PhoneNum, &book.Address)
	if err != nil {
		fmt.Println("输入错误")
		t.save(db)
	}
	err = book.Save(db)
	if err != nil {
		fmt.Println("保存失败，返回首页")
	} else {
		fmt.Println("保存成功，返回首页")
	}
	t.Start(db)
}