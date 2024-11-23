import { request } from "@/utils/service"
import type * as Table from "./types/table"

/** 增 */
export function createTableDataApi(data: Table.CreateOrUpdateTableRequestData) {
  return request({
    url: "createTableData",
    method: "post",
    data
  })
}

/** 删 */
export function deleteTableDataApi(id: string) {
  return request({
    url: "deleteTableData",
    method: "post",
    data: { id: id }
  })
}

/** 改 */
export function updateTableDataApi(data: Table.CreateOrUpdateTableRequestData) {
  return request({
    url: "updateTableData",
    method: "post",
    data
  })
}

/** 查 */
export function getTableDataApi(params: Table.TableRequestData) {
  return request<Table.TableResponseData>({
    url: "getTableData",
    method: "get",
    params
  })
}

export const tableDataApi = () => {
  const getTableData = async (params: Table.TableRequestData) => {
    return {
      data: {
        total: 100,
        list: [
          {
            id: "410000200305186428",
            username: "Dorothy Williams",
            phone: "14037163725",
            email: "s.uqxoxhglu@ixlehdf.ke",
            roles: "admin",
            status: false,
            createTime: "1970-01-19 20:39:47"
          },
          {
            id: "510000198209181479",
            username: "Barbara Moore",
            phone: "16465743611",
            email: "h.fnfk@ipusjsqp.bi",
            roles: "admin",
            status: false,
            createTime: "1996-11-20 12:55:53"
          },
          {
            id: "510000198009093749",
            username: "Kimberly Wilson",
            phone: "15814747451",
            email: "r.dggkx@bixgljk.tp",
            roles: "editor",
            status: true,
            createTime: "1971-09-15 20:48:54"
          },
          {
            id: "440000199005034166",
            username: "Susan Hernandez",
            phone: "14114148546",
            email: "t.jldveybh@jkbobmttof.aq",
            roles: "editor",
            status: false,
            createTime: "2010-11-02 05:16:27"
          },
          {
            id: "990000200301234624",
            username: "Charles Perez",
            phone: "17054953861",
            email: "j.yijezkv@rcf.za",
            roles: "admin",
            status: true,
            createTime: "2023-03-17 15:18:07"
          },
          {
            id: "620000197910178471",
            username: "Michael Taylor",
            phone: "16542833582",
            email: "e.soevf@pzvkwyk.kr",
            roles: "admin",
            status: false,
            createTime: "2017-09-18 10:23:21"
          },
          {
            id: "110000197403055012",
            username: "James Thomas",
            phone: "14146730200",
            email: "y.nnruijf@hloumyaox.travel",
            roles: "editor",
            status: false,
            createTime: "2024-04-21 23:30:21"
          },
          {
            id: "330000199611075931",
            username: "Jose Davis",
            phone: "18985377754",
            email: "u.uhzivmbct@lkebtn.net.cn",
            roles: "editor",
            status: true,
            createTime: "1993-08-21 13:30:05"
          },
          {
            id: "990000201002091322",
            username: "Brian Hall",
            phone: "16771865566",
            email: "h.zmlncplxj@otuxkjeig.mil",
            roles: "admin",
            status: false,
            createTime: "1985-12-13 10:41:31"
          },
          {
            id: "430000200511213443",
            username: "Dorothy Smith",
            phone: "18327843325",
            email: "a.xhro@sii.na",
            roles: "admin",
            status: false,
            createTime: "2015-03-05 03:27:19"
          }
        ]
      }
    }
  }
  const createTableData = async (data: Table.CreateOrUpdateTableRequestData) => {

  }
  const updateTableData = async (data: Table.CreateOrUpdateTableRequestData) => {

  }
  const deleteTableData = async (id: string) => {

  }

  return { getTableData, createTableData, updateTableData, deleteTableData }
}
