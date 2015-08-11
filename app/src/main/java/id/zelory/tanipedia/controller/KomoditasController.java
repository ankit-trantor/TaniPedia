/*
 * Copyright (c) 2015 Zetra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.zelory.tanipedia.controller;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import id.zelory.benih.controller.BenihController;
import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.model.Komoditas;
import id.zelory.tanipedia.network.TaniPediaService;

/**
 * Created by zetbaitsu on 7/31/15.
 */
public class KomoditasController extends BenihController<KomoditasController.Presenter>
{
    private List<Komoditas> listKomoditas;

    public KomoditasController(Presenter presenter)
    {
        super(presenter);
    }

    public void loadListKomoditas()
    {
        presenter.showLoading();
        TaniPediaService.pluck()
                .getApi()
                .getKomoditas()
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(listKomoditas -> {
                    this.listKomoditas = listKomoditas;
                    if (presenter != null)
                    {
                        presenter.showListKomoditas(listKomoditas);
                        presenter.dismissLoading();
                    }
                }, throwable -> {
                    if (presenter != null)
                    {
                        presenter.showError(throwable);
                        presenter.dismissLoading();
                    }
                });
    }

    @Override
    public void saveState(Bundle bundle)
    {
        bundle.putParcelableArrayList("listKomoditas", (ArrayList<Komoditas>) listKomoditas);
    }

    @Override
    public void loadState(Bundle bundle)
    {
        listKomoditas = bundle.getParcelableArrayList("listKomoditas");
        if (listKomoditas != null)
        {
            presenter.showListKomoditas(listKomoditas);
        } else
        {
            presenter.showError(new Throwable("List komoditas is null"));
        }
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showLoading();

        void dismissLoading();

        void showListKomoditas(List<Komoditas> listKomoditas);
    }
}